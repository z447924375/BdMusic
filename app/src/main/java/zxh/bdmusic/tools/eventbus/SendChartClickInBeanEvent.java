package zxh.bdmusic.tools.eventbus;

import zxh.bdmusic.bean.ChartClickInBean;

/**
 * Created by dllo on 16/10/10.
 */
public class SendChartClickInBeanEvent {
    private ChartClickInBean chartClickInBean;


    public ChartClickInBean getChartClickInBean() {
        return chartClickInBean;
    }

    public void setChartClickInBean(ChartClickInBean chartClickInBean) {
        this.chartClickInBean = chartClickInBean;
    }
}
