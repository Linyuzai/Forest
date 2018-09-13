package eason.linyuzai.forest.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eason.linyuzai.forest.R;
import eason.linyuzai.layoutmanager.FolderLayoutManager;

public class LayoutManagerTestActivity extends AppCompatActivity {

    FolderLayoutManager lm;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_manager_test);
        RecyclerView recyclerView = findViewById(R.id.rv_layout_manager);
        //recyclerView.setLayoutManager(new OverlayDragLayoutManager());
        lm = new FolderLayoutManager() {
            @Override
            public void runViewTransform(View view, float relativeY) {
                super.runViewTransform(view, relativeY);
            }
        };
        lm.setShowCount(5);
        recyclerView.setLayoutManager(lm);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TestAdapter());
        //LinearLayoutManager
    }

    class TestAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("TestAdapter", "onCreateViewHolder");
            TextView textView = new TextView(parent.getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT);
            lp.setMargins(20, 0, 20, 0);
            textView.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Log.d("TestAdapter", "onBindViewHolder:" + position);
            final String t = "这里判断当childCount不为0的时候遍历recyclerview的每一个child然后拿到当前每一个child的位置跟屏幕显示大小进行比较，这里屏幕的范围为0～屏幕的高，如果不在这个范围内就回收掉它等待复用。\n" +
                    "\n" +
                    "还记得一开始我们那个存储了所有view位置的集合吗，现在有它用武之地了，遍历所有的view跟当前偏移的屏幕显示大小进行比较，在这个范围内的,我们获取到view后并进行位置的填充，注意！这里屏幕显示大小的范围不再是0～屏幕的高了而是我们滚动时记录的 位置偏移量～位置偏移量+屏幕的高。\n" +
                    "\n" +
                    "可能这里你会有疑问，回收的view我们是怎么拿到的呢？就是这个方法getViewForPosition(), recyclerview有多级缓存，它会去recyclerview的所有缓存中去找，比如最先会在scap中找,如果找到会比较这个当前position跟scap缓存中viewHolder的position是否一致，如果一致直接返回这个viewholder并且不需要进行rebinding数据，如果找不到或者position不一致再去caches缓存中去找，如果所有的缓存中都找不到就会调用mAdapter.onCreateViewHolder()方法去创建一个全新的viewholder。\n" +
                    "\n" +
                    "具体细节可以看我这篇关于recyclerview回收分析： \n" +
                    "http://blog.csdn.net/boboyuwu/article/details/77148302\n" +
                    "\n" +
                    "到此为止，一个简单的自定义LinearLayoutManager就诞生了,并且特性和效果跟官方的基本一致，当然我们自然没有官方提供控件考虑的那么周到并且测试的也没有那么全面，毕竟官方的控件还是提供了非常多的功能，代码逻辑要更之复杂，但是基本的功能已经完全实现了，我们看下效果吧\n" +
                    "\n" +
                    "在onCreateViewHolder方法里并创建一个累加值打印下log观看下viewholder创建情况";
            TextView textView = (TextView) holder.itemView;
            textView.setText("Test:" + position + t);
            int color = 0;
            switch (position % 5) {
                case 0:
                    color = Color.RED;
                    break;
                case 1:
                    color = Color.YELLOW;
                    break;
                case 2:
                    color = Color.BLUE;
                    break;
                case 3:
                    color = Color.GRAY;
                    break;
                case 4:
                    color = Color.GREEN;
                    break;
            }
            textView.setBackgroundColor(color);
            textView.setOnClickListener(v -> {
                if (i % 2 == 0)
                    lm.expandView(v);
                else
                    lm.collapseView(v);
                i++;
            });
        }

        @Override
        public int getItemCount() {
            return 60;
        }
    }
}
