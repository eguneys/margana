package oyun.net.anagram.adapter;

import java.util.List;
import android.util.Log;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.AbsListView;

import android.support.v7.widget.RecyclerView;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

import oyun.net.anagram.helper.ResourceUtil;
import oyun.net.anagram.databinding.ItemCategoryBinding;

import oyun.net.anagram.widget.LetterView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private final Activity mActivity;
    private List<Category> mCategories;

    private final LayoutInflater mLayoutInflater;

    public CategoryAdapter(Activity activity) {
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
        updateCategories(activity);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }


    public Category getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCategories.get(position).getId().hashCode();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ItemCategoryBinding)DataBindingUtil
                              .inflate(mLayoutInflater, R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ItemCategoryBinding binding = holder.getBinding();
        Category category = getItem(position);
        binding.setCategory(category);
        binding.executePendingBindings();
        binding.categoryTitle
            .setText(ResourceUtil.getDynamicString(mActivity,
                                                   R.string.nbLetters,
                                                   category.getId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onClick(v, holder.getAdapterPosition());
                }
            });
        setScaleAnimation(holder.itemView, position);
    }

    private void setScaleAnimation(View view, int position) {
        view.setPivotY(view.getHeight());
        view.setScaleY(0);
        view.animate()
            .scaleY(1)
            .setDuration(200)
            .setStartDelay(position * 150)
            .start();
    }

    private void updateCategories(Activity activity) {
        mCategories = AnagramDatabaseHelper.getCategories(activity, false);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        
        private ItemCategoryBinding mBinding;
        
        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ItemCategoryBinding getBinding() {
            return mBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
