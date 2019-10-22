package awsm.awsmizng.u.alanguageapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.models.Upload;

public class ArchiveArticleRecyclerAdapter extends RecyclerView.Adapter<ArchiveArticleRecyclerAdapter.ViewHolder> {

    String TAG = "bhjtytt";
    private List<Upload> articles;
    private OnArticleListener mOnArticleListener;

    public ArchiveArticleRecyclerAdapter(List<Upload> articles, OnArticleListener onArticleListener) {
        this.articles = articles;
        this.mOnArticleListener = onArticleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_archive_article, viewGroup, false);
        return new ViewHolder(view, mOnArticleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvArticleTitle.setText(articles.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvArticleTitle;
        OnArticleListener mOnArticleListener;

        public ViewHolder(View itemView, OnArticleListener onArticleListener) {
            super(itemView);
            tvArticleTitle = itemView.findViewById(R.id.tvArticleTitle);
            mOnArticleListener = onArticleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnArticleListener.onArticleClick(getAdapterPosition());
        }
    }

    public void updateList(List<Upload> data) {
        articles = data;
        notifyDataSetChanged();
    }

    public interface OnArticleListener {
        void onArticleClick(int position);
    }
}
