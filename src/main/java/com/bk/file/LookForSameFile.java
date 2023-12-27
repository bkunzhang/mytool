package com.bk.file;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author bkunzh
 * @date 2023/12/7
 */
public class LookForSameFile {

    public static void main(String[] args) {
        printDirInfo(new File(DIR1));
        printDirInfo(new File(DIR2));

        // 删除重复的文件
        //deleteRepeatFileInDir1();
    }

    static void deleteRepeatFileInDir1() {
        List<File> filesOfDir1 = getAllFiles(new File(DIR1));
        // 相同目录去重，不一样，相同目录需要删除出现2次以上的
        //List<File> filesOfDir1 = getAllFiles(new File(DIR2));
        Set<String> dir1Names = new HashSet<>();
        Map<String, File> dir1Map = filesOfDir1.stream()
                .filter(file -> {
                    if (dir1Names.contains(file.getName())) {
                        return false;
                    }
                    dir1Names.add(file.getName());
                    return true;
                })
                .collect(Collectors.toMap(file -> file.getName(), file -> file));

        List<File> filesOfDir2 = getAllFiles(new File(DIR2));
        Set<String> dir2Names = new HashSet<>();
        Map<String, File> dir2Map = filesOfDir2.stream()
                .filter(file -> {
                    if (dir2Names.contains(file.getName())) {
                        return false;
                    }
                    dir2Names.add(file.getName());
                    return true;
                })
                .collect(Collectors.toMap(file -> file.getName(), file -> file));
        // 目录2中文件名在目录1出现统计
        System.out.println("目录2中文件名在目录1重复出现统计--------------");
        List<String> dir2FileNameSameInDir1 = dir2Map.keySet().stream()
                .filter(fileName -> dir1Map.keySet().contains(fileName))
                .collect(Collectors.toList());
        System.out.println("重复出现个数=" + dir2FileNameSameInDir1.size());
        System.out.println(dir2FileNameSameInDir1);
        // 重复文件占的大小
        AtomicReference<Long> maxSizeRef = new AtomicReference<>(0L);
        AtomicReference<File> maxSizeFileRef = new AtomicReference<>();
        double repeatedSizeGB = dir2FileNameSameInDir1.stream()
                .map(name -> dir2Map.get(name))
                .filter(file -> {
                    if (maxSizeRef.get() < file.length()) {
                        maxSizeRef.set(file.length());
                        maxSizeFileRef.set(file);
                    }
                    return true;
                })
                .mapToDouble(file -> file.length() * 1.0 / 1024 / 1024)
                .sum() / 1024;
        System.out.println("repeatedSizeGB=" + repeatedSizeGB);

        System.out.println("重复的最大文件信息::::::");
        System.out.println(maxSizeRef.get() * 1.0 / 1024 / 1024);
        System.out.println(maxSizeFileRef.get().getAbsolutePath());
        System.out.println("----------");

        dir2FileNameSameInDir1.stream()
                .filter(name -> dir1Map.get(name).length() == dir2Map.get(name).length())
                .forEach(name -> {
                    // log 删除:H:\000redmi_k30_photos\00pictures\Gallery\owner\善待我的胃\VID_20200204_211016.mp4 is ok? true
                    System.out.print("删除:" + dir1Map.get(name).getAbsolutePath()
                            + " is ok? ");
                    //System.out.print(dir1Map.get(name).delete());
                    System.out.println();
                });
    }

    static void printDirInfo(File dir) {
        System.out.println("目录" + dir.getAbsolutePath() + "信息:");
        List<File> allFiles = getAllFiles(dir);
        System.out.println("文件个数: " + allFiles.size());
        double totalFileSize = allFiles.stream().mapToDouble(file -> file.length() * 1.0 / 1024 / 1024).sum();
        System.out.println("所有文件大小(GB): " + totalFileSize / 1024);
        Set<String> fileNames = new HashSet<>();
        List<String> pathsRepeated = new ArrayList<>();
        allFiles.stream().forEach(fileVisited -> {
            if (fileNames.contains(fileVisited.getName())) {
                pathsRepeated.add(fileVisited.getAbsolutePath());
            } else {
                fileNames.add(fileVisited.getName());
            }
        });
        System.out.println("不重复文件个数: " + fileNames.size());

        System.out.println("重复文件的路径: " + pathsRepeated);
    }

    static List<File> getAllFiles(File dir) {
        List<File> filesInDir = new ArrayList<>();
        for (File fileVisited : dir.listFiles()) {
            if (fileVisited.isDirectory()) {
                filesInDir.addAll(getAllFiles(fileVisited));
            } else {
                filesInDir.add(fileVisited);
            }
        }
        return filesInDir;
    }

    private final static String DIR1 = "H:\\000redmi_k30_photos";

    private final static String DIR2 = "H:\\mi11-photos";
}
