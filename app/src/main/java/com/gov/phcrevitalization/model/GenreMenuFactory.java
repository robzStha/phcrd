package com.gov.phcrevitalization.model;

import com.gov.phcrevitalization.utils.Genre;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bugatti on 06/12/16.
 */

public class GenreMenuFactory {
    public static final String MENU1 = "National Free Services Program";
    public static final String MENU2 = "Orientation/Training on Standard Treatment Protocol";
    public static final String MENU3 = "Donation for printing and registration fee for district hospital, PHC and HP";
    public static final String MENU4 = "Reporting Program for Target Groups";
    public static final String MENU5 = "Special Health Programs for DAG VDCs";
    public static final String MENU6 = "Package of Essential Non-communicable Diseases";
    public static final String MENU7 = "Social Audit Program";
    public static final String MENU8 = "Community Health";
    public static final String MENU9 = "Urban Health Centre";
    public static final String MENU10 = "Urban FCHVs";
    public static final String MENU11 = "Regional Special Health Camps";
    public static final String MENU12 = "Employee Skills Enhancement Program";
    public static final String MENU13 = "Supervision and Monitoring";
    public static final String SUBMENU11 = "Free Essential Drug Procurement";
    public static final String SUBMENU12 = "Meeting of District Level Monitoring Committee";
    public static final String SUBMENU21 = "Regional Level Orientation/Training";
    public static final String SUBMENU22 = "District Level Orientation/Training";
    public static final String SUBMENU71 = "District Level Social Audit Program";
    public static final String SUBMENU72 = "Orienation on Social Audit";
    public static final String SUBMENU91 = "Donation for Building Construction for Urban Health";
    public static final String SUBMENU101 = "Cloth Allowance";
    public static final String SUBMENU102 = "Annual Review Meeting";
    public static final String SUBMENU103 = "FCHV Day";

    public static List<Genre> makeGenre(){
        return Arrays.asList(makeItem1(), makeItem2(), makeItem3(), makeItem4(),
                makeItem5(), makeItem6(), makeItem7(), makeItem8(),
                makeItem9(), makeItem10(), makeItem11(), makeItem12(), makeItem13());
    }

    private static Genre makeItem1() {
        return new Genre(MENU1, makeSubItem1());
    }

    private static List<SubItems> makeSubItem1() {
        SubItems subItems1 = new SubItems(SUBMENU11);
        SubItems subItems2 = new SubItems(SUBMENU12);
        return Arrays.asList(subItems1, subItems2);
    }

    private static Genre makeItem2() {
        return new Genre(MENU2, makeSubItem2());
    }

    private static List<SubItems> makeSubItem2() {
        SubItems subItems1 = new SubItems(SUBMENU21);
        SubItems subItems2 = new SubItems(SUBMENU22);
        return Arrays.asList(subItems1, subItems2);
    }

    private static Genre makeItem3() {
        return new Genre(MENU3, makeSubItemDummy());
    }

    private static List<SubItems> makeSubItem7() {
        SubItems subItems1 = new SubItems(SUBMENU71);
        SubItems subItems2 = new SubItems(SUBMENU72);
        return Arrays.asList(subItems1, subItems2);
    }

    private static Genre makeItem4() {
        return new Genre(MENU4, makeSubItemDummy());
    }
    private static Genre makeItem5() {
        return new Genre(MENU5, makeSubItemDummy());
    }
    private static Genre makeItem6() {
        return new Genre(MENU6, makeSubItemDummy());
    }
    private static Genre makeItem7() {
        return new Genre(MENU7, makeSubItem7());
    }
    private static Genre makeItem8() {
        return new Genre(MENU8, makeSubItemDummy());
    }
    private static Genre makeItem9() {
        return new Genre(MENU9, makeSubItem9());
    }
    private static Genre makeItem10() {
        return new Genre(MENU10, makeSubItem10());
    }
    private static Genre makeItem11() {
        return new Genre(MENU11, makeSubItemDummy());
    }
    private static Genre makeItem12() {
        return new Genre(MENU12, makeSubItemDummy());
    }
    private static Genre makeItem13() {
        return new Genre(MENU13, makeSubItemDummy());
    }

    private static List<SubItems> makeSubItem9() {
        SubItems subItems1 = new SubItems(SUBMENU91);
        return Arrays.asList(subItems1);
    }private static List<SubItems> makeSubItem10() {
        SubItems subItems1 = new SubItems(SUBMENU101);
        SubItems subItems2 = new SubItems(SUBMENU102);
        SubItems subItems3 = new SubItems(SUBMENU103);
        return Arrays.asList(subItems1,subItems2, subItems3);
    }private static List<SubItems> makeSubItem4() {
        SubItems subItems1 = new SubItems("");
        return Arrays.asList(subItems1);
    }
    private static List<SubItems> makeSubItemDummy() {
//        SubItems subItems1 = new SubItems("");
//        return Arrays.asList(new SubItems(""));
        return null;
    }

}
