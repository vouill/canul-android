package canul.android;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Chazz on 10/10/15.
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> {

    private String[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    public IndexAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public IndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_index, parent, false);
        //TODO add view size and stuff..

       ViewHolder holder = new ViewHolder((CardView) view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IndexAdapter.ViewHolder holder, int position) {
        TextView text = (TextView) holder.cardView.findViewById(R.id.info_text);
        text.setText(dataset[position]);
    }

    @Override
    public int getItemCount() {

        return dataset.length;
    }

}
