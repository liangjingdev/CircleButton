/**
 * Created by liangjing on 2017/8/7.
 * function:笔记
 */

public class note {

    /*Android解决自定义View获取不到焦点的情况

    引言：
        我们在使用Android View或者SurfaceView进行图形绘制，可以绘制各种各样我们喜欢的图形，然后满怀信心的给我们的View加上onTouchEvent、onKeyDown、onKeyUp让图形按照我们希望的进行移动。
        但是往往希望越大所受的打击也越大，在运行后我们杯具发现，我们的View根本无法获取触摸或者点击事件。（即触摸后什么操作也没做），为什么会这样呢？怎样解决这样的问题呢？本篇博客将详细的阐述一下其中的原因！

        1.首先说一下Android事件传递机制，哦不，确切的说应该是Java的事件传递机制（别紧张Android同样适用，因为Android的开发就是给予Java的）

          Android事件具有向下传递的特性，什么意思呢？比如说你有一个ViewGroup，而ViewGroup上又有一个View按钮，当点击View的时候，
          事件的传递机制是：Button按钮首先获取焦点（获取点击事件），然后传递给ViewGroup再然后就传递给Activity了。

其实在引言中提到的哪个问题就是由于这种事件传递机制引起的，因为View或者SurfaceView首先获取到了触摸事件，紧接着View或者SurfaceView将事件向下传递给Activity，由Activity捕获，不是因为事件没执行，只不过事件被Activity处理了。


  解决这种问题的方法有两种：

    方案一：

    1.在自定义View的构造方法中加上setFocusable(true);该方法的意思是让当前View获取焦点。例如：

    public GameView(Context context) {
        super(context);
        this.setFocusable(true);//允许获取上层焦点
    }
　　2.将View的触摸或者点击事件的返回值改为true。例如：

       public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
    方案二：

     在Activity的触摸或者点击事件中调用自定义View的触摸或者点击事件。

*/

}
