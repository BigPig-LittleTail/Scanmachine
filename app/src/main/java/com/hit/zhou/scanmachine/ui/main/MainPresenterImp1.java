package com.hit.zhou.scanmachine.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.Machine;
import com.hit.zhou.scanmachine.common.MyMessage;
import com.hit.zhou.scanmachine.common.NetService;
import com.hit.zhou.scanmachine.ui.main.home.HomeIView;
import com.hit.zhou.scanmachine.ui.main.home.HomePresenter;
import com.hit.zhou.scanmachine.ui.main.message.MessagePresenter;
import com.hit.zhou.scanmachine.ui.main.message.MessageView;
import com.hit.zhou.scanmachine.ui.main.message.MessageViewIView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/15.
 */

public class MainPresenterImp1 implements MainConstruct.MainPresenter {
    private Context context;
    private Messenger service;
    private boolean isBound;
    private MainConstruct.MainIView mainIView;
    private Messenger viewMessenger = new Messenger(new ReplyToServiceHandler(this));
    private ServiceConnection connection;

    private HomePresenter homePresenter;
    private MessagePresenter messagePresenter;


    public MainPresenterImp1(Context context, MainConstruct.MainIView mainIView){
        this.context = context;
        this.mainIView = mainIView;
    }

    @Override
    public void initThreeItemView(HomeIView homeIView, MessageViewIView messageViewIView) {
        homePresenter = (HomePresenter) homeIView.getPresenter();
        //:TODO phone
        homePresenter.requestMachineInfo(service,"");
        messagePresenter =  (MessagePresenter) messageViewIView.getPresenter();
        messagePresenter.requestMessage(service,0,"");
        messagePresenter.requestMessage(service,1,"");
        messagePresenter.requestMessage(service,2,"");
    }

    private static class ReplyToServiceHandler extends Handler {
        private WeakReference<MainPresenterImp1> reference;

        ReplyToServiceHandler(MainPresenterImp1 mainPresenterImp1){
            reference = new WeakReference<>(mainPresenterImp1);
        }

        @Override
        public void handleMessage(Message message){
            MainPresenterImp1 mainPresenterImp1 = reference.get();
            switch (message.what){
                case NetService.MSG_RESULT_HOME_VIEW:{
                    ArrayList<Machine> list = (ArrayList<Machine>) message.getData().getSerializable(NetService.RESULT_MACHINE_LIST);
                    ((HomeIView)mainPresenterImp1.homePresenter.getView()).showMachineInfo(list);
                    break;
                }
                case NetService.MSG_RESULT_MESSAGE_LIST:{
                    ArrayList<MyMessage> myMessages = (ArrayList<MyMessage>) message.getData().getSerializable(NetService.MESSAGE_LIST);
                    int itemPosition = message.getData().getInt(NetService.MESSAGE_LIST_TYPE);
                    ((MessageViewIView)mainPresenterImp1.messagePresenter.getView()).showMessageRecyclerView(myMessages,itemPosition);
                }
                case NetService.MSG_ERROR_NET_ERROR:{
                    //:TODO 改成错误处理
                    ArrayList<Machine> list = new ArrayList<>();
                    list.add(new Machine("1","1","机器1","92.3","12","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542444315646&di=92f13d7550c6419c765a5b2465b3be5b&imgtype=0&src=http%3A%2F%2Fwww.chinairn.com%2FUserFiles%2Fimage%2F20170216%2F20170216145829_3729.jpg"));
                    list.add(new Machine("1","2","机器2","90.1","14","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542444469221&di=6f5fbecc1a310228b2b427d0d114b075&imgtype=0&src=http%3A%2F%2Fwww.hfxykj.com%2Fupload%2Fimage%2F20160910%2F20160910145229282928.jpg"));
                    list.add(new Machine("1","3","机器3","88.0","23","https://thumbs.dreamstime.com/b/%E4%BA%A7%E4%B8%9A%E6%9C%BA%E5%99%A8-51178370.jpg"));

                    ArrayList<MyMessage> myMessagesOne = new ArrayList<MyMessage>();
                    myMessagesOne.add(new MyMessage("智能机器未来发展","http://img95.699pic.com/photo/50060/5542.jpg_wh860.jpg","讨论未来智能机器的发展前景"));
                    myMessagesOne.add(new MyMessage("工业4.0的内涵","http://img06.tooopen.com/images/20161228/tooopen_sy_193758948572.jpg","工业4.0的内涵是什么，作为大学生可以做出什么贡献"));
                    myMessagesOne.add(new MyMessage("阿拉丁跑分的可靠性","http://img2.imgtn.bdimg.com/it/u=3859421672,511986628&fm=200&gp=0.jpg","在阿拉丁中跑分的优点是什么？"));
                    myMessagesOne.add(new MyMessage("终于轮到我了，我的机器跑了90分，如何？","http://bpic.588ku.com/back_pic/03/52/05/5457932788c7203.jpg","论坛中的平均跑分是怎样的分布呢？"));
                    myMessagesOne.add(new MyMessage("收购高质量机器","http://img4.duitang.com/uploads/item/201412/05/20141205164857_fZyQh.thumb.700_0.jpeg","跑分90以上的机器所有者请联系我"));
                    myMessagesOne.add(new MyMessage("工业云还是阿拉丁","http://img0.pcgames.com.cn/pcgames/1208/15/2609720_dZO3F.jpg","在未来，工业云和阿拉丁谁会占领市场"));


                    ArrayList<MyMessage> myMessagesTwo = new ArrayList<MyMessage>();
                    myMessagesTwo.add(new MyMessage("智能机器未来发展","http://img95.699pic.com/photo/50060/5542.jpg_wh860.jpg","讨论未来智能机器的发展前景"));

                    ArrayList<MyMessage> myMessagesThree = new ArrayList<MyMessage>();
                    myMessagesThree.add(new MyMessage("远航公司","http://img4q.duitang.com/uploads/item/201409/14/20140914234550_jB5PJ.thumb.700_0.png","你们的机器出售吗"));
                    myMessagesThree.add(new MyMessage("城际铁路公司","http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=049363aca3773912d02b8d229070ec6d/b21bb051f8198618c4eaca9540ed2e738bd4e676.jpg","新的传感器可以部署了"));
                    myMessagesThree.add(new MyMessage("BigPig_LittleTail","http://img5.duitang.com/uploads/item/201410/15/20141015213816_AtjJJ.thumb.700_0.jpeg","在吗"));

                    ((HomeIView)mainPresenterImp1.homePresenter.getView()).showMachineInfo(list);
                    ((MessageViewIView)mainPresenterImp1.messagePresenter.getView()).showMessageRecyclerView(myMessagesOne,0);
                    ((MessageViewIView)mainPresenterImp1.messagePresenter.getView()).showMessageRecyclerView(myMessagesTwo,1);
                    ((MessageViewIView)mainPresenterImp1.messagePresenter.getView()).showMessageRecyclerView(myMessagesThree,2);


                    break;
                }
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service = new Messenger(iBinder);
                isBound = true;
                Message message = Message.obtain();
                message.what = NetService.REPLY_TO_SERVICE_CONNECTION;
                message.replyTo = viewMessenger;
                try {
                    service.send(message);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                mainIView.bindServiceCallback(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                service = null;
                isBound = false;
                mainIView.bindServiceCallback(false);
            }
        };
    }

    @Override
    public void onStart() {
        Intent intent = new Intent(context,NetService.class);
        context.bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        if(isBound){
            context.unbindService(connection);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IView getView() {
        return mainIView;
    }
}
