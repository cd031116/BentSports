package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.ConstantValues;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.widget.pull.LoadMoreFooterView;
import com.lvr.library.holder.BaseViewHolder;
import com.lvr.library.recyclerview.HRecyclerView;
import com.lvr.library.recyclerview.OnLoadMoreListener;
import com.lvr.library.recyclerview.OnRefreshListener;
import com.zhl.network.RxSchedulers;

import java.util.ArrayList;

/**
 * Created by dawn on 2018/3/20.
 */

public class CardNewsFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    private int TYPE_GUGU_NORMAL = 1;
    private int TYPE_GUGU_GRID = 2;
    private int index;
    private int page = 1;
    private HRecyclerView hRecyclerView;
    private LoadMoreFooterView loadMoreFooterView;

    public static CardNewsFragment newInstance(int position) {
        CardNewsFragment fragment = new CardNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantValues.KEY_FRAGMENT_INDEX, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.card_news_fragment_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        index = (int) getArguments().get(ConstantValues.KEY_FRAGMENT_INDEX);
        hRecyclerView = (HRecyclerView) view.findViewById(R.id.h_list);
        hRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hRecyclerView.setLoadMoreEnabled(true);
        hRecyclerView.setOnLoadMoreListener(this);
        hRecyclerView.setOnRefreshListener(this);
        loadMoreFooterView = (LoadMoreFooterView) hRecyclerView.getLoadMoreFooterView();

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRefresh() {
//        page = 1;
//        if (guguList == null)
//            guguList = new ArrayList<>();
//        Observable<HuiquResult<GuguEntity>> guguDataObserver;
//        if (TextUtils.isEmpty(member_id))
//            guguDataObserver = BaseApi.getDefaultService(getActivity())
//                    .getGuguDataWithoutUser(index, page++);
//        else
//            guguDataObserver = BaseApi.getDefaultService(getActivity())
//                    .getGuguDataWithUser(index, page++, Integer.parseInt(member_id));
//        guguDataObserver
//                .map(new HuiquRxFunction<GuguEntity>())
//                .compose(RxSchedulers.<GuguEntity>io_main())
//                .subscribeWith(new RxObserver<GuguEntity>(getActivity(), TAG, 2, true) {
//                    @Override
//                    public void onSuccess(int whichRequest, GuguEntity guguEntity) {
//                        if (guguEntity != null && guguEntity.getGugu() != null && guguEntity.getGugu().size() > 0) {
//                            hRecyclerView.setRefreshing(false);
//                            guguList.clear();
//                            guguList.addAll(guguEntity.getGugu());
//                            setGuguView(guguList, 1);
//                        }
//                    }
//
//                    @Override
//                    public void onError(int whichRequest, Throwable e) {
//
//                    }
//                });
    }

    @Override
    public void onLoadMore() {
//        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
//        BaseApi.getDefaultService(getActivity())
//                .getGuguDataWithoutUser(index, page++)
//                .map(new HuiquRxFunction<GuguEntity>())
//                .compose(RxSchedulers.<GuguEntity>io_main())
//                .subscribeWith(new RxObserver<GuguEntity>(getActivity(), TAG, 1, false) {
//                    @Override
//                    public void onSuccess(int whichRequest, GuguEntity guguEntity) {
//                        if (guguEntity != null && guguEntity.getGugu() != null && guguEntity.getGugu().size() > 0) {
//                            hRecyclerView.setRefreshing(false);
//                            guguList.addAll(guguEntity.getGugu());
//                            setGuguView(guguList, 2);
//                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                        } else
//                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
//                    }
//
//                    @Override
//                    public void onError(int whichRequest, Throwable e) {
//                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
//                    }
//                });
    }

//    private void setGuguView(List<NewEntity> gugu, int type) {
//        adapter = multiSetting(gugu);
//        if (type == 1)
//            hRecyclerView.setAdapter(adapter);
//    }

//    private MultiItemCommonAdapter<NewEntity> multiSetting(final List<NewEntity> guguList) {
//        MultiItemTypeSupport<NewEntity> support = new MultiItemTypeSupport<NewEntity>() {
//            @Override
//            public int getLayoutId(int itemType) {
//                if (itemType == TYPE_GUGU_VIDEO) {
//                    return R.layout.gugu_item_video;
//                } else if (itemType == TYPE_GUGU_NORMAL) {
//                    return R.layout.gugu_item_normal;
//                } else
//                    return R.layout.gugu_item_gridview;
//
//            }
//
//            @Override
//            public int getItemViewType(int position, NewEntity newEntity) {
//                if (!TextUtils.isEmpty(guguList.get(position).getVideo_path())) {
//                    return TYPE_GUGU_VIDEO;
//                } else if (guguList.get(position).getImg() != null && guguList.get(position).getImg().size() == 1)
//                    return TYPE_GUGU_NORMAL;
//                else
//                    return TYPE_GUGU_GRID;
//            }
//        };
//
//        final MultiItemCommonAdapter<NewEntity> mAdapter = new MultiItemCommonAdapter<NewEntity>(getActivity(), guguList, support) {
//            @Override
//            public void convert(final BaseViewHolder holder, final int position) {
//                TextView guguTitle = (TextView) holder.getView(R.id.gugu_title);
//                ImageView NormalHeadImg = (ImageView) holder.getView(R.id.gugu_head_img);
//                TextView guguDingwei = (TextView) holder.getView(R.id.gugu_dingwei);
//                TextView guFavorTextView = (TextView) holder.getView(R.id.gugu_dianzan);
//
//                RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.default_head_img);
//                Glide.with(NormalHeadImg.getContext()).load(guguList.get(position).getHeadimgurl())
//                        .apply(requestOptions)
//                        .into(NormalHeadImg);
//
//                holder.getItemView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//                    @Override
//                    public void onViewAttachedToWindow(View v) {
//
//                    }
//
//                    @Override
//                    public void onViewDetachedFromWindow(View v) {
//                        if (!TextUtils.isEmpty(guguList.get(position).getVideo_path())) {
//                            if (NiceVideoPlayerManager.instance(mContext).ispaly() && NiceVideoPlayerManager.instance(mContext).getPosition() == position) {
//                                NiceVideoPlayerManager.instance(mContext).releaseNiceVideoPlayer();
//                            }
//                        }
//                    }
//                });
//                if (guguList.get(position).getFavor_status() == 0)
//                    setZanBg(guFavorTextView, R.drawable.zan2);
//                else
//                    setZanBg(guFavorTextView, R.drawable.zan);
//
//                holder.setText(R.id.gugu_name, guguList.get(position).getNickname());
//                holder.setText(R.id.gugu_time, guguList.get(position).getAdd_time());
//                holder.setText(R.id.gugu_pinglun, guguList.get(position).getComment_num() + "");
//                holder.setText(R.id.gugu_dianzan, guguList.get(position).getFavor_num() + "");
//
//                favorMap.put(position, guguList.get(position).getFavor_num());
//                holder.getView(R.id.gugu_dianzan).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (!TextUtils.isEmpty(member_id)) {
//                            if ("0".equals(member_id)) {
//                                loginBefore();
//                            } else {
//                                if (guguList.get(position).getFavor_status() == 0) {
//                                    setFavor(position, holder);
//                                } else {
//                                    setCancelFavor(position, holder);
//                                }
//                            }
//                        } else
//                            loginBefore();
//
//                    }
//                });
//
//                if (!TextUtils.isEmpty(guguList.get(position).getGugu_content())) {
//                    guguTitle.setVisibility(View.VISIBLE);
//                    guguTitle.setText(guguList.get(position).getGugu_content());
//                }
////                if (!TextUtils.isEmpty(guguList.get(position).getHeadimgurl())) {
////                    Log.d("sss", "convert: " + guguList.get(position).getHeadimgurl());
//                if (TextUtils.isEmpty(guguList.get(position).getGugu_content()))
//                    guguTitle.setVisibility(View.GONE);
//                if (!TextUtils.isEmpty(guguList.get(position).getAddress())) {
//                    guguDingwei.setVisibility(View.VISIBLE);
//                    guguDingwei.setText(guguList.get(position).getAddress());
//                } else if (TextUtils.isEmpty(guguList.get(position).getAddress()))
//                    guguDingwei.setVisibility(View.GONE);
//                if (!TextUtils.isEmpty(guguList.get(position).getVideo_path())) {
//                    PlayManager playManager = new PlayManager(holder.getItemView(), mContext);
//                    TxVideoPlayerController controller = new TxVideoPlayerController(mContext, position);
//
//                    playManager.setController(controller);
//                    playManager.bindData(guguList.get(position));
//
//                } else if (guguList.get(position).getImg() != null && guguList.get(position).getImg().size() == 1) {
//                    ImageView guguPic = (ImageView) holder.getView(R.id.gugu_pic);
//
//                    if (!TextUtils.isEmpty(guguList.get(position).getImg().get(0))) {
//                        guguPic.setVisibility(View.VISIBLE);
//                        RequestOptions options = new RequestOptions()
//                                .centerCrop()
//                                .error(R.drawable.default_img)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL);
//                        Glide.with(guguPic.getContext())
//                                .load(guguList.get(position).getImg().get(0))
//                                .apply(options)
//                                .into(guguPic);
//                        guguPic.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), GuguImgActivity.class);
//                                intent.putExtra("type", 0);
//                                intent.putExtra("img", guguList.get(position).getImg().get(0));
//                                startActivity(intent);
//                            }
//                        });
//                    } else guguPic.setVisibility(View.GONE);
//                } else {
//                    if (guguList.get(position).getImg() != null) {
//                        RecyclerView gridList = (RecyclerView) holder.getView(R.id.gugu_list);
//                        RecyclerView gridTwoList = (RecyclerView) holder.getView(R.id.gugu_two_list);
//
//                        GuGuGridRecyclerViewAdapter guguAdapter = new GuGuGridRecyclerViewAdapter(getActivity(), guguList.get(position).getImg());
//                        if (guguList.get(position).getImg().size() == 4 || guguList.get(position).getImg().size() == 2) {
//                            gridTwoList.setVisibility(View.VISIBLE);
//                            gridList.setVisibility(View.GONE);
//                            gridTwoList.setLayoutManager(new MyGridLayoutManager(getActivity(), 2));
//                            gridTwoList.setAdapter(guguAdapter);
//                        } else {
//                            gridTwoList.setVisibility(View.GONE);
//                            gridList.setVisibility(View.VISIBLE);
//                            gridList.setLayoutManager(new MyGridLayoutManager(getActivity(), 3));
//                            gridList.setAdapter(guguAdapter);
//                        }
//                        guguAdapter.setOnItemClickListener(new GuGuGridRecyclerViewAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int pos) {
//                                Intent intent = new Intent(getActivity(), GuguImgActivity.class);
//                                intent.putExtra("type", 1);
//                                intent.putStringArrayListExtra("img_list", (ArrayList<String>) guguList.get(position).getImg());
//                                startActivity(intent);
//                            }
//                        });
//                    }
//                }
//                holder.getItemView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int itemViewType = getItemViewType(position);
//                        int article_id = guguList.get(position).getArticle_id();
//                        Intent intent = new Intent(getActivity(), GuguDetailActivity.class);
//                        intent.putExtra("article_id", article_id);
//                        intent.putExtra("itemViewType", itemViewType);
//                        startActivity(intent);
//                        Log.d(TAG, "onClick: " + itemViewType);
//                    }
//                });
//            }
//
//            private void setFavor(final int position, final BaseViewHolder holder) {
//                BaseApi.getDefaultService(getActivity())
//                        .clickFavor(1, guguList.get(position).getArticle_id(), Integer.parseInt(member_id))
//                        .map(new HuiquRxFunction<FavorEntity>())
//                        .compose(RxSchedulers.<FavorEntity>io_main())
//                        .subscribe(new RxObserver<FavorEntity>(getActivity(), TAG, 3, false) {
//                            @Override
//                            public void onSuccess(int whichRequest, FavorEntity favorEntity) {
//                                if (favorEntity.getFavorStatus() == 1) {
//                                    guguList.get(position).setFavor_status(1);
//                                    Integer favor_num = favorMap.get(position);
//                                    favor_num += 1;
//                                    favorMap.put(position, favor_num);
//                                    TextView textView = (TextView) holder.getView(R.id.gugu_dianzan);
//                                    holder.setText(R.id.gugu_dianzan, String.valueOf(favor_num));
//                                    setZanBg(textView, R.drawable.zan);
//                                }
//                            }
//
//                            @Override
//                            public void onError(int whichRequest, Throwable e) {
//
//                            }
//                        });
//            }
//
//            private void setCancelFavor(final int position, final BaseViewHolder holder) {
//                BaseApi.getDefaultService(getActivity())
//                        .clickCancelFavor(1, guguList.get(position).getArticle_id(), Integer.parseInt(member_id))
//                        .map(new HuiquRxFunction<CancelFavorEntity>())
//                        .compose(RxSchedulers.<CancelFavorEntity>io_main())
//                        .subscribe(new RxObserver<CancelFavorEntity>(getActivity(), TAG, 4, false) {
//                            @Override
//                            public void onSuccess(int whichRequest, CancelFavorEntity favorEntity) {
//                                if (favorEntity.getCancelStatus() == 1) {
//                                    guguList.get(position).setFavor_status(0);
//                                    Integer favor_num = favorMap.get(position);
//                                    favor_num -= 1;
//                                    favorMap.put(position, favor_num);
//                                    holder.setText(R.id.gugu_dianzan, String.valueOf(favor_num));
//                                    TextView textView = (TextView) holder.getView(R.id.gugu_dianzan);
//                                    setZanBg(textView, R.drawable.zan2);
//                                }
//                            }
//
//                            @Override
//                            public void onError(int whichRequest, Throwable e) {
//
//                            }
//                        });
//            }
//        };
//        mAdapter.notifyItemInserted(guguList.size() + 1);
//        return mAdapter;
//    }
}
