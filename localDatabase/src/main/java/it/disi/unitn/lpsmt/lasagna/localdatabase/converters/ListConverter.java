package it.disi.unitn.lpsmt.lasagna.localdatabase.converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;

public class ListConverter {

    @NonNull
    @TypeConverter
    public static String fromEventList(@NonNull List<String> list) {
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < list.size(); i++) {
            str.append(list.get(i));
            if(i < list.size() - 1) {
                str.append(",");
            }
        }

        return str.toString();
    }

    @NonNull
    @TypeConverter
    public static List<String> toEventList(String str) {
        if(str == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();
        String[] arr = str.split(",");
        return new ArrayList<>(Arrays.asList(arr));
    }

    @NonNull
    @TypeConverter
    public static String fromLuogoEventoList(@NonNull List<LuogoEv> evList) {
        StringBuilder res = new StringBuilder();

        for(LuogoEv e: evList) {
            res.append(e.toString()).append("; ");
        }

        return res.toString();
    }

    @NonNull
    @TypeConverter
    public static ArrayList<LuogoEv> toLuogoEventoList(String str) {
        if(str == null) {
            return new ArrayList<>();
        }

        ArrayList<LuogoEv> evList = new ArrayList<>();
        Type listType = new TypeToken<List<String>>() {}.getType();
        String[] arr = str.split("; ");
        for (String s : arr) {
            String[] arr1 = s.split(", ");
            String[] arr2 = arr1[2].split(" ");

            LuogoEv l = new LuogoEv(arr1[0], arr1[1], Integer.parseInt(arr2[0]), arr2[1], arr2[2], Integer.parseInt(arr1[3]),
                    arr1[4], arr1[5], Integer.parseInt(arr1[6]), Boolean.valueOf(arr1[7]));
            evList.add(l);
        }

        return evList;
    }
}
