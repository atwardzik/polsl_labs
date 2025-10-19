/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.twardzik.artur.bugTracker;

import controller.BugTracker;

import model.IssueManager;
import view.BugTrackerView;
import view.CliEnglishView;
import view.CliGermanView;

import java.util.Scanner;

/**
 * Class for the entry point
 * @author Artur Twardzik
 * @version 0.1
 */
public class BugTrackerApp {

    /**
     * Main function – start point
     * @param args should be with format "[app] lang DE/EN"
     */
    public static void main(String[] args) {
        String view = "";
        if (args.length < 2 || !args[0].equals("lang")) {
            System.out.println("Unknown parameters");
        } else {
            view = args[1];
        }

        BugTrackerView bugTrackerView = null;
        while (true) {
            boolean flag = false;

            switch (view) {
                case "EN":
                    bugTrackerView = new CliEnglishView();
                    flag = true;
                    break;
                case "DE":
                    bugTrackerView = new CliGermanView();
                    flag = true;
                    break;
                default:
                    System.out.print("Please specify start language [EN/DE]: ");

                    Scanner scanner = new Scanner(System.in);
                    view = scanner.nextLine();
                    break;
            }

            if (flag) {
                IssueManager issueManager = new IssueManager();
                BugTracker bugTracker = new BugTracker(issueManager, bugTrackerView);

                bugTracker.run();
                break;
            }
        }
    }
}
