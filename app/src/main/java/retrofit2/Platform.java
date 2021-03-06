/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

class Platform {
    private static final Platform PLATFORM = findPlatform();

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException ignored) {
        }
//        try {
//            Class.forName("java.util.Optional");
//            return new Java8();
//        } catch (ClassNotFoundException ignored) {
//        }
        try {
            Class.forName("org.robovm.apple.foundation.NSObject");
            return new IOS();
        } catch (ClassNotFoundException ignored) {
        }
        return new Platform();
    }

    Executor defaultCallbackExecutor() {
        return null;
    }

    CallAdapter.Factory defaultCallAdapterFactory(Executor callbackExecutor) {
        if (callbackExecutor != null) {
            return new ExecutorCallAdapterFactory(callbackExecutor);
        }
        return DefaultCallAdapterFactory.INSTANCE;
    }

    boolean isDefaultMethod(Method method) {
        return false;
    }

    Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, Object... args)
            throws Throwable {
        throw new UnsupportedOperationException();
    }

    static class Android extends Platform {
        @Override
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        /**
         * 将返回的适配类型默认为Call类型,
         * e.g. 如果使用RxJava的话，就可以通过配置
         * .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
         * 将配置类型改成Observable。）
         */
        @Override
        CallAdapter.Factory defaultCallAdapterFactory(Executor callbackExecutor) {
            return new ExecutorCallAdapterFactory(callbackExecutor);
        }

        /**
         * 返回的是用于执行 Callback 的 线程池。
         * 可以看到 MainThreadExecutor 获取了主线程的 Looper 并构造了一个主线程的 Handler，
         * 调用 Callback 时会将该请求 post 到主线程上去执行。
         * 这就解释了为什么请求后完成的回调都是在主线中。
         */
        static class MainThreadExecutor implements Executor {
            private final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable r) {
                handler.post(r);
            }
        }
    }

    static class IOS extends Platform {
        @Override
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        @Override
        CallAdapter.Factory defaultCallAdapterFactory(Executor callbackExecutor) {
            return new ExecutorCallAdapterFactory(callbackExecutor);
        }

        static class MainThreadExecutor implements Executor {
            private static Object queue;
            private static Method addOperation;

            static {
                try {
                    // queue = NSOperationQueue.getMainQueue();
                    Class<?> operationQueue = Class.forName("org.robovm.apple.foundation.NSOperationQueue");
                    queue = operationQueue.getDeclaredMethod("getMainQueue").invoke(null);
                    addOperation = operationQueue.getDeclaredMethod("addOperation", Runnable.class);
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }

            @Override
            public void execute(Runnable r) {
                try {
                    // queue.addOperation(r);
                    addOperation.invoke(queue, r);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new AssertionError(e);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    } else if (cause instanceof Error) {
                        throw (Error) cause;
                    }
                    throw new RuntimeException(cause);
                }
            }
        }
    }
}
