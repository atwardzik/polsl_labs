/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.twardzik.artur.prototyp01;

import controller.BugTracker;

import model.IssueManager;
import view.BugTrackerView;
import view.CliGermanView;

/**
 *
 * @author SuperStudent-PL
 */
public class TwardzikArturPrototyp01 {

    public static void main(String[] args) {
        //model view controller
        IssueManager issueManager = new IssueManager();
        BugTrackerView bugTrackerView = new CliGermanView();
        BugTracker bugTracker = new BugTracker(issueManager, bugTrackerView);

        bugTracker.run();
    }
}
