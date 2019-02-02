package pl.jwojcik.gascompanion.utils;

import java.util.Comparator;

import pl.jwojcik.gascompanion.models.Price;

public class PriceComparator implements Comparator<Price> {

    @Override
    public int compare(Price price, Price t1) {
        return t1.getInsertDt().compareTo(price.getInsertDt());
    }
}
