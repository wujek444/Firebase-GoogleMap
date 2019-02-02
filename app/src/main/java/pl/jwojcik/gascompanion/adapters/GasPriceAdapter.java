package pl.jwojcik.gascompanion.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.models.Price;


public class GasPriceAdapter extends ArrayAdapter<Price> {

    private List<Price> dataList = new ArrayList<>();
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private LayoutInflater inflater;

    public GasPriceAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<Price> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Price getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GasPriceAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gas_price, null);
            holder = new GasPriceAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GasPriceAdapter.ViewHolder) convertView.getTag();
        }
        Price item = getItem(position);
        holder.tvName.setText("dodał " + item.getUserEmail());
        holder.tvPrice.setText(String.format("%.2f%s", item.getValue(), " PLN/litr"));
        holder.tvDate.setText("Dnia " + df.format(item.getInsertDt()));

        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvPrice;
        public TextView tvDate;

        public ViewHolder(View view) {
            tvName = view.findViewById(R.id.tv_name);
            tvPrice = view.findViewById(R.id.tv_price);
            tvDate = view.findViewById(R.id.tv_date);
        }
    }
}
