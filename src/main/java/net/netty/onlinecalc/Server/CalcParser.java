package net.netty.onlinecalc.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FrozeRain on 27.07.2019.
 */
class CalcParser {
    private static Pattern pattern = Pattern.compile("(\\-?\\d*\\.?\\d*)(\\D)((\\((\\-\\d*\\.?\\d*)\\))|(\\d*\\.?\\d*))");

    static String parseAndSolve(String example) throws NumberFormatException{
        Matcher matcher = pattern.matcher(example);
        if (matcher.find()){
            double value1 = new Double(matcher.group(1));
            double value2;
            if (matcher.group(6) == null){
                value2 = new Double(matcher.group(5));
            } else {
                value2 = new Double(matcher.group(6));
            }
            System.out.println("> Example: " + value1 + "|" + matcher.group(2) + "|" + value2);
            switch (matcher.group(2)){
                case "+":{
                    return Double.toString(value1 + value2);
                }
                case "-":{
                    return Double.toString(value1 - value2);
                }
                case "*":{
                    return Double.toString(value1 * value2);
                }
                case "/":{
                    return Double.toString(value1 / value2);
                }
            }
        }
        return "Incorrect!";
    }
}
