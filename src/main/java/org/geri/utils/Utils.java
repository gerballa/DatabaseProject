package org.geri.utils;

import java.util.List;

public class Utils {
    public static boolean containsUniqueElements(List<String> stringList) {
        return stringList.stream().distinct().count() == stringList.size();
    }
}
