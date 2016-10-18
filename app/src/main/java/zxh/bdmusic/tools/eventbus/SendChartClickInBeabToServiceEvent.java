package zxh.bdmusic.tools.eventbus;

import zxh.bdmusic.bean.ChartClickInBean;

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
