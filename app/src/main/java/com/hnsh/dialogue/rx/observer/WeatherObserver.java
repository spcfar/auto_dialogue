package com.hnsh.dialogue.rx.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public  class WeatherObserver<T> implements Observer {


    Disposable d ;
   // public abstract void onSuccess(ResponseBody o);

   // public abstract void onFailure(Throwable e);


    @Override
    public void onSubscribe(Disposable d) {
      this.d = d;
    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        d.dispose();
    }
}
