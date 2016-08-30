package com.yushealth.prostate.fingerdanceforprostate;

public class FingerAction {

    private final int DURATION = 30;  //  单次延时 3分钟
    private final int DURATION_PAUSE = 5; // 切换手指延时 5秒
    private final int FACTOR = 30;

    private int cycle_count;
    private int cycle_count_cur;
    private int duration_cur;
    private int duration_pause_cur;

    private boolean finished;

    private int drawable;
    private int drawable_cur;

    // 阶段
    private enum Stage {
        RUN, PAUSE
    };
    private Stage stage;

    // 构造函数
    public FingerAction(int cycle_count, int drawable) {
        this.cycle_count = cycle_count;
        this.drawable = drawable;
        this.drawable_cur = drawable;

        reset();
    }

    // 复位
    public void reset() {
        duration_cur = DURATION;
        cycle_count_cur = cycle_count;

        finished = false;
        stage = Stage.RUN;
    }

    // 结束
    public boolean isFinished() {
        return finished;
    }

    public void run(int tick) {
        switch (stage) {
            case RUN:
                duration_cur -= tick;
                // 完成1个周期
                if (duration_cur == 0) {
                    cycle_count_cur--;

                    stage = Stage.PAUSE;
                    drawable_cur = R.drawable.none;  // 手指无动作

                    duration_pause_cur = DURATION_PAUSE;
                    duration_cur = DURATION;
                }
                break;

            case PAUSE:
                duration_pause_cur -= tick;
                if (duration_pause_cur == 0) {
                    stage = Stage.RUN;
                    drawable_cur = R.drawable.stage_0;
                }
                break;
            // switch
        }

        // 完成所有周期
        if (cycle_count_cur == 0) {
            finished = true;
        }
        //
    }


    // 返回drawable
    public int get_drawable() {
        return drawable_cur;
    }

    public void setPause() {
        stage = Stage.PAUSE;
    }

    public void setContinue() {
        stage = Stage.RUN;
    }

    // 已完成次数
    public int getCycle_count() {
        return cycle_count_cur * FACTOR;
    }
// end
}
