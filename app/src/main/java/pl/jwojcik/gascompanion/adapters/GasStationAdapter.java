package pl.jwojcik.gascompanion.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.models.GasStation;
import pl.jwojcik.gascompanion.utils.StringUtils;


public class GasStationAdapter extends ArrayAdapter<GasStation> {

    private LayoutInflater inflater;
    private List<GasStation> mList = new ArrayList<>();

    public GasStationAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<GasStation> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GasStation getItem(int position) {
        return mList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gas_station, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GasStation item = getItem(position);
        holder.tvName.setText(item.getName());
        if (StringUtils.isNotEmpty(item.getAddress()))
            holder.tvAddress.setText(item.getAddress());

        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvAddress;

        public ViewHolder(View view) {
            tvName = view.findViewById(R.id.tv_name);
            tvAddress = view.findViewById(R.id.tv_address);
        }
    }

}
