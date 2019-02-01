package pl.jwojcik.gascompanion.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.models.Price;


public class GasPriceAdapter extends RecyclerView.Adapter<GasPriceAdapter.ViewHolder> {

    private List<Price> dataList;
    private Context mContext;
    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");



    public GasPriceAdapter(@NonNull Context context, List<Price> list) {
        super();
        this.mContext = context;
        this.dataList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gas_price, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Price item = dataList.get(position);
        holder.tvName.setText("dodał " + item.getUserEmail());
        holder.tvPrice.setText(String.format("%.0f%s", item.getValue(), " PLN/litr"));
        holder.tvDate.setText("Dnia " + df.format(item.getInsertDt()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView tvName;
        public TextView tvPrice;
        public TextView tvDate;
        public ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvPrice = view.findViewById(R.id.tv_price);
            tvDate = view.findViewById(R.id.tv_date);
        }
    }

}
