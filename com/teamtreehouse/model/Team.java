package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Team implements Comparable<Team>{
  private String mName;
  private String mCoach;
  private SortedSet<Player> mPlayers;
  
  public Team(String name, String coach){
    mName = name;
    mCoach = coach;
    mPlayers = new TreeSet<Player>();
  }
  
  public String getName(){
    return mName;
  }
  
  public String getCoach(){
    return mCoach;
  }
  
  public void addPlayer(Player player) {
    if(getPlayers().size() == 11){
      System.out.println("Team full, player not added.");
      return;
    }
    mPlayers.add(player);
  }
  
  public void removePlayer(Player player){
   mPlayers.remove(player); 
  }
  
  public SortedSet<Player> getPlayers(){
    return mPlayers;
  }
  
  @Override
  public int compareTo(Team other) {
    if(equals(other)){
      return 0; 
    }
    int nameCmp = mName.compareTo(other.mName);
    if(nameCmp == 0){
      return mCoach.compareTo(other.mCoach);
    }
    return nameCmp;
  }
  
}