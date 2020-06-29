package com.boll.tyelauncher.easytrans;


import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public interface IDataReportInterface extends IInterface {
    void onActiveEvent(String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void onControlEvent(String str, String str2, String str3, Map map) throws RemoteException;

    void onEvent(String str, String str2, Map map) throws RemoteException;

    void onEventJson(String str, String str2, String str3, String str4) throws RemoteException;

    void onEventList(String str, String str2, String str3) throws RemoteException;

    void onStatsEvent(String str, String str2, String str3, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IDataReportInterface {
        private static final String DESCRIPTOR = "com.iflytek.easytrans.dependency.aidl.logagent.IDataReportInterface";
        static final int TRANSACTION_onActiveEvent = 6;
        static final int TRANSACTION_onControlEvent = 2;
        static final int TRANSACTION_onEvent = 1;
        static final int TRANSACTION_onEventJson = 4;
        static final int TRANSACTION_onEventList = 3;
        static final int TRANSACTION_onStatsEvent = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDataReportInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDataReportInterface)) {
                return new Proxy(obj);
            }
            return (IDataReportInterface) iin;
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onEvent(data.readString(), data.readString(), data.readHashMap(getClass().getClassLoader()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onControlEvent(data.readString(), data.readString(), data.readString(), data.readHashMap(getClass().getClassLoader()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onEventList(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onEventJson(data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onStatsEvent(data.readString(), data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onActiveEvent(data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDataReportInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override
            public void onEvent(String eventType, String name, Map data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(eventType);
                    _data.writeString(name);
                    _data.writeMap(data);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void onControlEvent(String eventType, String controlCode, String eventName, Map parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(eventType);
                    _data.writeString(controlCode);
                    _data.writeString(eventName);
                    _data.writeMap(parameters);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void onEventList(String eventType, String controlCode, String jsonArrayLogs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(eventType);
                    _data.writeString(controlCode);
                    _data.writeString(jsonArrayLogs);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void onEventJson(String eventType, String controlCode, String eventName, String jsonObject) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(eventType);
                    _data.writeString(controlCode);
                    _data.writeString(eventName);
                    _data.writeString(jsonObject);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void onStatsEvent(String eventType, String controlCode, String name, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(eventType);
                    _data.writeString(controlCode);
                    _data.writeString(name);
                    _data.writeInt(count);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onActiveEvent(String name, String appId, String appVersion, String channelId, String bundleInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(appId);
                    _data.writeString(appVersion);
                    _data.writeString(channelId);
                    _data.writeString(bundleInfo);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
