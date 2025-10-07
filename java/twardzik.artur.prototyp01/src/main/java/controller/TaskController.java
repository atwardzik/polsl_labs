/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.Scanner;
import model.Issue;

/**
 *
 * @author SuperStudent-PL
 */
public class TaskController {
    public Issue createTask() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Task name: ");
        String taskName = scanner.nextLine();
        
        System.out.println("Task description: ");
        String taskDesc = scanner.nextLine();
        
        System.out.println("Task user: ");
        String taskUser = scanner.nextLine();
        
        return new Issue(taskUser, taskName, taskDesc);
    }
}
