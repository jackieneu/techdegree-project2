package com.teamtreehouse.model;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Team;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Organizer {
  private SortedSet<Player> mPlayers; 
  private BufferedReader mReader;
  private List<Team> mTeams;
  private double maxTeams;
  
  private Map<String, String> mMenu;
  
  public Organizer(Player[] players){
    mPlayers = new TreeSet<Player>(Arrays.asList(players));
    maxTeams = Math.floor(mPlayers.size()/11);
    mReader = new BufferedReader(new InputStreamReader(System.in));
    mMenu = new LinkedHashMap<String, String>();
    mMenu.put("Create", "Create a new team");
    mMenu.put("Add", "Add a player to a team");
    mMenu.put("Remove", "Remove a player from a team");
    mMenu.put("Report", "View a report of a team by height");
    mMenu.put("Balance", "View the League Balance Report");
    mMenu.put("Roster", "View roster");
    mMenu.put("Quit", "Exits the program");
    mTeams = new ArrayList<Team>();
    Collections.sort(mTeams);
  }
  
  private String promptAction() throws IOException{
    System.out.printf("%nMenu%n");
    for(Map.Entry<String, String> option: mMenu.entrySet()){
      System.out.printf("%s - %s %n",
                        option.getKey(),
                        option.getValue());
    }
    System.out.printf("%nSelect an option: ");
    String choice = mReader.readLine();
    System.out.println("");
    return choice.trim().toLowerCase();    
  }
  
  public void run(){
    String choice = "";
    do{
      try{
        choice = promptAction();
        switch(choice){
         case "create":
            if(mTeams.size()>= maxTeams){
              System.out.println("More teams cannot be created than there are players.");
              break;
            }
            Team team = promptNewTeam();
            mTeams.add(team);
            Collections.sort(mTeams);
            break;
         case "add":
            Player player = promptPlayer(mPlayers);
            team = promptTeam();
            team.addPlayer(player);
            mPlayers.remove(player);
            break;
         case "remove":
            team = promptTeam();
            player = promptPlayer(team.getPlayers());
            team.removePlayer(player);
            mPlayers.add(player);
            //Collections.sort(mPlayers);
            break;
          case "report":
            team = promptTeam();
            printReport(team.getPlayers());
            break;
         case "balance":
            printBalanceReport();
            break;
         case "roster":
            team = promptTeam();
            printRoster(team.getPlayers());
            break;
         case "quit":
            System.out.println("Goodbye!");
            break;
         default:
            System.out.printf("Unknown choice: '%s'. Try again. %n%n%n", choice);
        }
      }catch(IOException ioe){
        System.out.println("Problem with input");
        ioe.printStackTrace();
      }
    }while(!choice.equals("quit"));
  }
  
  private Team promptNewTeam() throws IOException{
    System.out.print("What is the team name?  ");
    String name = mReader.readLine();
    System.out.print("What is the coach name?  ");
    String coach = mReader.readLine();
    System.out.printf("Team %s coached by %s added.%n", name, coach);
    return new Team(name, coach);
  }
  
  private Player promptPlayer(SortedSet<Player> options) throws IOException{
    System.out.println("Available players:");
    int counter = 1;
    String experience;
    for(Player option : options){
      if(option.isPreviousExperience()){
         experience = "experienced";
      }else{
        experience = "inexperienced";
      }
      System.out.printf("%d.)  %s %s (%d inches - %s) %n", 
                        counter, 
                        option.getFirstName(),
                        option.getLastName(),
                        option.getHeightInInches(),
                        experience);
      counter++;
    }
    System.out.print("Your choice:  ");
    String optionAsString = mReader.readLine();
    int choice = Integer.parseInt(optionAsString.trim());
    int count = 1;
    for(Player option : options){
      if(count == choice){
        return option;
      }
      count++;
    }
    return null;
  }
  
  private Team promptTeam()  throws IOException{
    System.out.println("Available teams:");
    int counter = 1;
    for(Team option : mTeams){
      System.out.printf("%d.)  Team %s coahced by %s %n", 
                        counter, 
                        option.getName(),
                        option.getCoach());
      counter++;
    }
    System.out.print("Your choice:  ");
    String optionAsString = mReader.readLine();
    int choice = Integer.parseInt(optionAsString.trim());
    System.out.println("");
    return mTeams.get(choice - 1);
  }
  
   public void printReport(SortedSet<Player> players){
     Map<String, ArrayList<Player>> heightMap = new LinkedHashMap<>();
     ArrayList<Player> list30_40 = new ArrayList<Player>();
     ArrayList<Player> list41_46 = new ArrayList<Player>();
     ArrayList<Player> list47_50 = new ArrayList<Player>();
     for(Player player : players){
       if(player.getHeightInInches() >= 35 && player.getHeightInInches() <= 40){
           list30_40.add(player);
       }
       if(player.getHeightInInches() >= 41 && player.getHeightInInches() <= 46){
           list41_46.add(player);
        }
       if(player.getHeightInInches() >= 47 && player.getHeightInInches() <= 50){
           list47_50.add(player);
        }
      }
     heightMap.put("35-40 inches", list30_40);
     heightMap.put("41-46 inches", list41_46);
     heightMap.put("47-50 inches", list47_50);
     for(Map.Entry<String, ArrayList<Player>> entry : heightMap.entrySet()){
       System.out.printf("Players at %s:%n",entry.getKey());
       for(Player player: entry.getValue()){
        System.out.printf("\t%s %s (%d inches) %n", 
                        player.getFirstName(),
                        player.getLastName(),
                        player.getHeightInInches());
       }
       System.out.printf("%d player(s) at %s.%n%n", 
                         entry.getValue().size(), 
                         entry.getKey());
     }
  }
  
  public void printBalanceReport(){
    Map<String, int[]> balance = new LinkedHashMap<>();
    for(Team team : mTeams){
      int[] experience = new int[2];
      for(Player player : team.getPlayers()){
        if(player.isPreviousExperience()){
          experience[0]++;
        }else{
          experience[1]++;
        }
      }
      balance.put(team.getName(), experience);
    }
    System.out.printf("%nTeam Experience: (experienced:inexperienced)%n");
    for(Map.Entry<String, int[]> option : balance.entrySet()){
      System.out.printf("%s: (%d:%d)",
                        option.getKey(),
                        option.getValue()[0],
                        option.getValue()[1]);
      if(option.getValue()[0]+option.getValue()[1] > 0){
        System.out.printf("  |  Experienced Players: %.1f%%%n",
                          100.0 * ( (double) option.getValue()[0]) / (option.getValue()[0] + option.getValue()[1]));
      }else{
        System.out.printf("%n");
      }
    }
  }
  
  public void printRoster(Set<Player> options){
    //Collections.sort(options);
    String experience;
    for(Player option : options){
      if(option.isPreviousExperience()){
         experience = "experienced";
      }else{
        experience = "inexperienced";
      }
      System.out.printf("%s %s (%d inches - %s) %n", 
                        option.getFirstName(),
                        option.getLastName(),
                        option.getHeightInInches(),
                        experience);    
    }
  }
  
}
    
