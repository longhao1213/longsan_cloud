package com.longsan;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRotation {
    private List<String> users; // 用户列表
    private int userIndex = 0; // 当前用户索引，用于循环队列

    public ScheduleRotation(List<String> userList) {
        this.users = new ArrayList<>(userList);
    }

    // 从用户列表中循环获取用户
    private List<String> getUsers(int count) {
        List<String> selectedUsers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            selectedUsers.add(users.get(userIndex % users.size()));
            userIndex++;
        }
        return selectedUsers;
    }

    public void simulateScheduling(List<Integer> workdaysList) {
        // 预先设置第一周的发布人
        List<String> publishers = getUsers(workdaysList.get(0));
        
        for (int week = 0; week < workdaysList.size(); week++) {
            // 根据下周的工作日数量，确定这周的回复人
            int nextWeekWorkdays = week + 1 < workdaysList.size() ? workdaysList.get(week + 1) : workdaysList.get(0);
            List<String> responders = getUsers(nextWeekWorkdays);

            System.out.println("第 " + (week + 1) + " 周:");
            System.out.println("发布人: " + publishers);
            System.out.println("回复人: " + responders);

            // 更新下周的发布人为这周的回复人
            publishers = new ArrayList<>(responders);
        }
    }

    public static void main(String[] args) {
        List<String> users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            users.add("User" + i);
        }

        List<Integer> workdaysList = List.of(5, 6,4,5); // 示例：第一周有5个工作日，第二周有6个工作日
        ScheduleRotation rotation = new ScheduleRotation(users);
        rotation.simulateScheduling(workdaysList);
    }
}
