package canul.android.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import canul.android.interfaces.EndlessScrollInterface;

/**
 * Created by Chazz on 16/10/15.
 */
public class ArticlesEndlessScrollListener extends EndlessScrollListener {

    private final EndlessScrollInterface endlessScrollInterface;

    public ArticlesEndlessScrollListener(LinearLayoutManager manager, EndlessScrollInterface endlessScrollInterface){
        super(manager);
        this.endlessScrollInterface= endlessScrollInterface;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onLoadMore() {
        this.endlessScrollInterface.onLoadMore();
    }
}
