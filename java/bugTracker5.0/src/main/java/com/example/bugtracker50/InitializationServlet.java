package com.example.bugtracker50;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "Initialization", urlPatterns = "/init", loadOnStartup = 1)
public class InitializationServlet extends HttpServlet {

    enum RepositoryType {
        MEMORY,
        MOCK
    }

    @Override
    public void init() {

        RepositoryType repositoryType = RepositoryType.MOCK;

        var context = getServletContext();

        List<User> users = new ArrayList<>();
        users.add(new User("Artur", "Twardzik", "at"));
        users.get(0).addRole(UserRole.REVIEWER);
        users.add(new User("J", "Twardzik", "jt"));

        IssueManager manager = new IssueManager();
        manager.addIssue(
                new Issue(
                        "Off-by-One Stack Write in syscall handler",
                        """
                                <html dir="ltr">
                                <head></head>
                                <body contenteditable="true" style="font-family: Ubuntu; font-size: 14px;">
                                <h2>Bug description</h2>
                                    <p>
                                        A boundary miscalculation in the ARM exception entry path causes a\s
                                        one-byte overwrite at the top of the kernel stack when handling synchronous\s
                                        exceptions. The off-by-one error appears when saving the SPSR and LR registers\s
                                        during nested exception entry, leading to occasional stack frame corruption.
                                    </p>
                                
                                <h2>Repro steps</h2>
                                    <ol>
                                    <li>Enable high exception frequency (e.g., trigger repeated page faults).</li>
                                    <li>Run the system under load with preemption enabled.</li>
                                    <li>Observe intermittent kernel panics or silent data corruption during\s
                                        nested exception handling.</li>
                                    <li>Enable stack canaries or KASAN; corruption is typically reported at\s
                                        the exception entry prologue.</li>
                                    </ol>
                                
                                <h2>Expected behaviour</h2>
                                    <p>
                                        Exception entry should correctly preserve all callee-saved registers without\s
                                        overwriting memory beyond the allocated stack frame. No stack corruption, and no\s
                                        kernel instability.
                                    </p>
                                
                                <h2>Target</h2>
                                    <p>
                                        ARMv8-A (AArch64) exception handling path, specifically the EL1 synchronous\s
                                        exception entry prologue.
                                    </p>
                                
                                <h2>Additional context</h2>
                                    N/A
                                </body></html>
                                """,
                        users.get(0), //reporter
                        LocalDate.now().plusDays(7).atStartOfDay(),
                        users.get(0), //assignee
                        BugStatus.IN_PROGRESS,
                        Priority.HIGH,
                        Set.of("kernel", "memory", "stack-smash")
                )
        );
        manager.addIssue(
                new Issue(
                        "VFS Initramfs Mount Issue",
                        """
                                <html dir="ltr">
                                <head></head>
                                <body contenteditable="true" style="font-family: Ubuntu; font-size: 14px;">
                                <h2>Bug description</h2>
                                    <p>
                                        During early boot, the Virtual File System layer intermittently fails to mount\s
                                        the initramfs as the root filesystem. The issue appears to stem from a race in\s
                                        the initialization sequence, where the VFS attempts to access the initramfs\s
                                        superblock before its in-memory structure has been fully populated. This results\s
                                        in a mount failure and fallback to an emergency shell.
                                    </p>
                                
                                <h2>Repro steps</h2>
                                    <ol>
                                        <li>Boot the system with an initramfs image of typical size (compressed or uncompressed).</li>
                                        <li>Enable parallel initialization features such as accelerated decompression or async probing.</li>
                                        <li>Observe that in some boots, the VFS reports that the initramfs root could not be mounted.</li>
                                        <li>Kernel logs show that the VFS mount request occurs before the initramfs is fully prepared.</li>
                                    </ol>
                                
                                <h2>Expected behaviour</h2>
                                    <p>
                                        The initramfs should always be mounted as the root filesystem once its image is\s
                                        decompressed and loaded into memory. The VFS should not attempt the mount until\s
                                        the initramfs superblock and inode structures have been completely initialized.
                                    </p>
                                
                                <h2>Target</h2>
                                    <p>
                                        Virtual File System (VFS) initialization path during early boot, specifically\s
                                        the sequence involving initramfs unpacking and rootfs mount registration.
                                    </p>
                                
                                <h2>Additional context</h2>
                                    <ul>
                                        <li>The issue presents more frequently on systems with fast multicore CPUs.</li>
                                        <li>Recent changes to async initialization appear to influence timing.</li>
                                        <li>Boot-time debugging and increased log level (e.g., earlyprintk) help\s
                                            expose inconsistent ordering between initramfs and VFS setup.</li>
                                    </ul>
                                </body></html>
                                """,
                        users.get(0), //reporter
                        LocalDate.now().plusDays(7).atStartOfDay(),
                        users.get(0), //assignee
                        BugStatus.REOPENED,
                        Priority.HIGH,
                        Set.of("kernel", "memory", "stack-smash")
                )
        );
        manager.addIssue(
                new Issue("Ex. Closed Issue", "<h1>This was closed</h1>",
                        users.get(1), null, null,
                        BugStatus.CLOSED, Priority.HIGH, null)
        );
        manager.addIssue(
                new Issue("Buffer overflow", "Serious damage", users.get(1))
        );

        context.setAttribute("IssueManager", manager);
    }

}

