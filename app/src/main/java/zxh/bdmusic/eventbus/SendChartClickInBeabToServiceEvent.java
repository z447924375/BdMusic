package zxh.bdmusic.eventbus;

import zxh.bdmusic.musiclibrary.chart.ChartClickInBean;

/**
 * Created by dllo on 16/10/15.
 */
public class SendChartClickInBeabToServiceEvent {
    ChartClickInBean bean;

    public ChartClickInBean getBean() {
        return bean;
    }

    public void setBean(ChartClickInBean bean) {
        this.bean = bean;
    }
}
