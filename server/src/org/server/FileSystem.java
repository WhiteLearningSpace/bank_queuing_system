//package org.serverexample;
//
//import java.io.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class FileSystem {
//    private static String src = "data.txt";
//
//    public static void saveData() {
//        try {
//            OutputStream fos = new FileOutputStream(src);
//            String qinfo = Number.getQueueInfo();
//            fos.write(qinfo.getBytes());
//            fos.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static String readData() {
//        String qinfo = null;
//        try {
//            File file = new File(src);
//            if (file.createNewFile()){
//                System.out.println("创建文件");
//            };
//            InputStreamReader isr = new InputStreamReader(new FileInputStream(src));
//
//            StringBuffer sb = new StringBuffer();
//            while (isr.ready()) {
//                sb.append((char) isr.read());
//                // 转成char加到StringBuffer对象中
//            }
//            qinfo = String.valueOf(sb);
//            isr.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return qinfo;
//    }
//
//    public static void reloadData() {
//        String regex = "\\w+=(\\[(?:[\\[\\w,\\s\\]]*|[\\w,\\s]*)]|\\w*)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(readData());
//
//        while (matcher.find()) {
//            String[] strS = matcher.group()
//                    .split("=");
//
//            switch (strS[0]) {
//                case "numberQueue" -> {
//                    String[] strArr = strS[1].replaceAll("[\\[\\]]", "")
//                            .split(",");
//                    if ("".equals(strArr[0])) {
//
//                        break;
//                    }
//
//                    for (String s : strArr) {
//                        Number.numberQueue.add(Integer.parseInt(s.trim()));
//                    }
//                }
//                case "callingList" -> {
//                    String[] strArr = strS[1].replaceAll("[\\[\\]]", "")
//                            .split(",");
//                    if ("".equals(strArr[0])) {
//
//                        break;
//                    }
//
//                    for (int i = 0; i < strArr.length; i += 2) {
//                        int i1 = Integer.parseInt(strArr[i].trim());
//                        int i2 = Integer.parseInt(strArr[i + 1].trim());
//                        Number.callingList.add(new Integer[]{i1, i2});
//                    }
//                }
//                case "passNumberCount" -> Number.passNumberCount = Integer.parseInt(strS[1]);
//                case "callNumberCount" -> Number.callNumberCount = Integer.parseInt(strS[1]);
//                case "numberCount" -> Number.numberCount = Integer.parseInt(strS[1]);
//            }
//        }
//    }
//}
