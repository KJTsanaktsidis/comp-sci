package net.kjtsanaktsidis.prac2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictMenuDriver {

    private SkipListDict<Integer, String> sl;

    public static void main(String[] args) throws IOException{
        (new DictMenuDriver()).run();
    }



    public void run() throws IOException {
        //loop and do stuff
        sl = new SkipListDict<>();
        boolean shouldExit = false;

        Pattern regex = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

        BufferedReader brin = new BufferedReader(new InputStreamReader(System.in));

        while (!shouldExit) {
            System.out.print("> ");
            String inline = brin.readLine();

            //This regex shamelessly ripped off from
            // http://stackoverflow.com/questions/7804335/split-string-on-spaces-except-if-between-quotes-i-e-treat-hello-world-as
            //because life is too short to dick around with string splitting at the command line
            List<String> alist = new ArrayList<>();
            Matcher argMatcher = regex.matcher(inline);
            while (argMatcher.find()) {
                alist.add(argMatcher.group(1).replace("\"", ""));
            }

            if (alist.size() == 0) {
                continue;
            }

            //arg 1 is the command
            switch (alist.get(0)) {
                case "h":
                    printHelp();
                    break;
                case "i":
                    if (alist.size() == 2) {
                        //key only
                        Integer key = Integer.parseInt(alist.get(1));
                        sl.put(key, "");
                    }
                    else if (alist.size() == 3) {
                        Integer key = Integer.parseInt(alist.get(1));
                        String value = alist.get(2);
                        sl.put(key, value);
                    }
                    else {
                        System.out.println("\tWrong number of args to insert");
                    }
                    break;
                case "p":
                    sl.resetSearchPathTrace();
                    showPrintout();
                    break;
                case "s":
                case "sp":
                    sl.resetSearchPathTrace();
                    if (alist.size() == 2) {
                        Integer key = Integer.parseInt(alist.get(1));
                        if (sl.containsKey(key)) {
                            System.out.println("\tFound");
                        }
                        else {
                            System.out.println("\tNot found");
                        }
                        if (alist.get(0).equals("sp")) {
                           showPrintout();
                        }
                    }
                    else {
                        System.out.println("\tWrong number of arguments to search");
                    }
                    break;
                case "d":
                    if (alist.size() == 2) {
                        Integer key = Integer.parseInt(alist.get(1));
                        String delres = sl.remove(key);
                        if (delres == null) {
                            System.out.println("\tNot found");
                        }
                        else {
                            System.out.println("\tDeleted");
                        }
                    }
                    else {
                        System.out.println("\tWrong number of arguments to delete");
                    }
                    break;
                case "x":
                    shouldExit = true;
                    break;
                default:
                    System.out.println("\tUnknown command");
            }
        }
    }

    public void showPrintout() throws IOException {
        File tmp = File.createTempFile("printout", ".svg");
        OutputStreamWriter sw = new OutputStreamWriter(new FileOutputStream(tmp));
        SvgSkipListRenderer.SkipListToSVG(sl, sw);
        sw.close();
        Runtime.getRuntime().exec("\"C:\\Program Files\\Internet Explorer\\iexplore.exe\" \"" +
                tmp.getAbsoluteFile() + "\"");
    }

    public void printHelp() {
        System.out.println("\tCommands:");
        System.out.println("\t\th:                  Help");
        System.out.println("\t\ti <key>:            Insert key");
        System.out.println("\t\ti <key> <value>:    Insert key/value pair");
        System.out.println("\t\tp:                  Print current state of the skiplist");
        System.out.println("\t\ts <key>:            Search for the key");
        System.out.println("\t\tsp <key>:           Search for key and print search path");
        System.out.println("\t\td <key>:            Delete key if it exists");
        System.out.println("\t\tx:                  Exit");
    }


}
