package com.example.nightout.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nightout.R;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MovieVH> {

    private static final String TAG = "MovieAdapter";
    List<Recycle> recycleList;

    public RecycleAdapter(List<Recycle> recycleList) {
        this.recycleList = recycleList;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guy, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {

        Recycle recycle = recycleList.get(position);
        holder.titleTextView.setText(recycle.getTitle());
        holder.yearTextView.setText(recycle.getYear());
        holder.plotTextView.setText(recycle.getPlot());

        boolean isExpanded = recycleList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return recycleList.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {

        private static final String TAG = "MovieVH";

        ConstraintLayout expandableLayout;
        TextView titleTextView, yearTextView, plotTextView;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            plotTextView = itemView.findViewById(R.id.plotTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Recycle recycle = recycleList.get(getAdapterPosition());
                    recycle.setExpanded(!recycle.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}
