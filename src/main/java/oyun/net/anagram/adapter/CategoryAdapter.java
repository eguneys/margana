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

    public static final int ANIM_DURATION = 200;
    public static final int ANIM_DELAY = 25;

    private OnItemClickListener mOnItemClickListener;

    private final Activity mActivity;
    private List<Category> mCategories;

    private final LayoutInflater mLayoutInflater;

    private int mLastAnimatedPosition;

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

    public final void notifyItemChanged(String id) {
        updateCategories(mActivity);
        int position = getItemPositionById(id);
        notifyItemChanged(position);
        Log.e("YYY item changed", "" + mCategories.get(position).getStars());
    }

    public int getItemPositionById(String id) {
        for (int i = 0; i < mCategories.size(); i++) {
            if (mCategories.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
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

        if (category.getName().length() == 1) {

            binding.categoryTitle
                .setText(ResourceUtil.getDynamicString(mActivity,
                                                       R.string.nbLetters,
                                                       category.getName()));
        } else {
            if (category.getName() == "mixed") {
                binding.categoryTitle.setText(R.string.mixed);
            } else {
                binding.categoryTitle.setText(category.getName());
            }
        }

        int stars = category.getStars();
        binding.categoryStars.setNbStar(stars);


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
        if (position <= mLastAnimatedPosition) {
            view.setScaleY(1);
            return;
        }
        mLastAnimatedPosition = position;

        view.setPivotY(view.getHeight());
        view.setScaleY(0);
        view.animate()
            .scaleY(1)
            .setDuration(ANIM_DURATION)
            .setStartDelay(position * ANIM_DELAY)
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
