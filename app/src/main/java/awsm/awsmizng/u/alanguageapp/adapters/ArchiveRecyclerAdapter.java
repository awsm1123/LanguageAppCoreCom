package awsm.awsmizng.u.alanguageapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import awsm.awsmizng.u.alanguageapp.R;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.ViewHolder> {

    private ArrayList<String> theme = new ArrayList<String>();
    private OnThemeListener mOnThemeListener;

    public ArchiveRecyclerAdapter(ArrayList<String> theme, OnThemeListener onThemeListener) {
        this.theme = theme;
        this.mOnThemeListener = onThemeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_archive_theme, viewGroup, false);
        return new ViewHolder(view, mOnThemeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvTheme.setText(theme.get(i));
    }

    @Override
    public int getItemCount() {
        return theme.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTheme;
        OnThemeListener mOnThemeListener;

        public ViewHolder(View itemView, OnThemeListener onThemeListener) {
            super(itemView);
            tvTheme = itemView.findViewById(R.id.tvThemeName);
            mOnThemeListener = onThemeListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnThemeListener.onThemeClick(getAdapterPosition());
        }
    }

    public interface OnThemeListener{
        void onThemeClick(int position);
    }
}
