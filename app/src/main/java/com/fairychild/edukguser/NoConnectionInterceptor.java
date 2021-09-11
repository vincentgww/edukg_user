package com.fairychild.edukguser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NoConnectionInterceptor implements Interceptor {

    private Context context;

    public NoConnectionInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!isConnectionOn()) {
            throw new NoConnectivityException();
        } else if(!isInternetAvailable()) {
            throw new NoInternetException();
        } else {
            return chain.proceed(chain.request());
        }
    }

    private boolean isInternetAvailable() {
        try {
            Integer timeoutMs = 1500;
            Socket sock = new Socket();
            InetSocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    private boolean isConnectionOn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities connection = connectivityManager.getNetworkCapabilities(network);

        return connection != null &&
                (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }

    static class NoConnectivityException extends IOException {
        @Nullable
        @Override
        public String getMessage() {
            return "No network available, please check your WiFi or Data connection";
        }
    }

    static class NoInternetException extends IOException {
        @Nullable
        @Override
        public String getMessage() {
            return "No internet available, please check your connected WIFi or Data";
        }
    }
}
