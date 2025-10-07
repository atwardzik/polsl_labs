/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.twardzik.artur.prototyp01;

import controller.TaskController;
import java.util.List;
import java.util.Scanner;
import model.Issue;
import model.IssueManager;
import view.EnglishView;

/**
 *
 * @author SuperStudent-PL
 */
public class TwardzikArturPrototyp01 {

    public static void main(String[] args) {
        //model view controller
        IssueManager issueManager = new IssueManager();
        EnglishView englishView = new EnglishView();
        
        for (int i = 0; i < 2; ++i) {
            englishView.printActionView();

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            int taskID;

            switch (choice) {
                case 1:
                    TaskController taskController = new TaskController();
                    Issue issue = taskController.createTask();
                    issueManager.addTask(issue);
                    break;
                case 2:
                    System.out.println("Give task ID: ");
                    taskID = scanner.nextInt();

                    System.out.println("Give task User: ");
                    String user = scanner.next();

                    try {
                        issueManager.assignTask(taskID, user);
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }

                    break;
                case 3:
                    System.out.println("Give task ID: ");
                    taskID = scanner.nextInt();
                    try {
                        issueManager.finishTask(taskID);
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }

                    break;
                default:
                    System.out.println("Your choice is not correct.");
                    break;
            }
        }
        
        List<Issue> allIssues = issueManager.getAllTasks();
        for (int i = 0; i < allIssues.size(); ++i) {
            System.out.println(allIssues.get(i).getTaskName() + " is " + allIssues.get(i).getTaskFinishedState());
        }
    }
}
