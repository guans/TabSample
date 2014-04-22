
package app.tabsample;

import java.util.ArrayList;

import com.tencent.tencentmap.streetviewsdk.animation.AnimGLSet;
import com.tencent.tencentmap.streetviewsdk.animation.ScaleResumeAnimGL;
import com.tencent.tencentmap.streetviewsdk.animation.TranslateAnimGL;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;
import com.tencent.tencentmap.streetviewsdk.overlay.model.ItemModel;

import android.graphics.Bitmap;

/**
 * һ���Լ�ʵ�ֵ�overlay��example
 * 
 * @author michaelzuo
 */
public class CustomerOverlay extends ItemizedOverlay {

    private ArrayList<CustomPoiData> mPois;

    public CustomerOverlay(ArrayList<CustomPoiData> pois) {
        this.mPois = pois;
    }

    @Override
    public int size() {
        return mPois.size();
    }

    @Override
    public void onTap(int index, float x, float y) {

        // test ����ĳ��poi��Ϣ
        Bitmap bitmap = Bitmap.createBitmap(mPois.get(1).marker);
        
        CustomPoiData poi = mPois.get(index);
        poi.updateMarker(bitmap, bitmap.toString());
        refresh(index);
    }

    @Override
    public ItemModel getItem(int index) {
        final CustomPoiData poi = mPois.get(index);
        if (poi == null)
            return null;

        ItemModel item = new CustomerItem(poi.latE6, poi.lonE6, poi.heightOffset);
        item.setAdapter(new ItemModel.IItemMarkerAdapter() {

            @Override
            public int getMarkerWidth() {
                return poi.marker.getWidth();
            }

            @Override
            public int getMarkerHeight() {
                return poi.marker.getHeight();
            }

            @Override
            public Bitmap getMarker(int state) {
                return poi.marker;
//                if (state == CustomerItem.STATE_PRESSED && poi.markerPressed != null) {
//                    return poi.markerPressed;
//                } else {
//                }
            }

            @Override
            public void onGetMarker(boolean suc) {

            }

            @Override
            public String getMarkerUID() {
                return poi.uid;
            }
        });

        TranslateAnimGL translateAnim = new TranslateAnimGL(0, 0, 180, 0, 400);
        // ScaleAnimGL scaleAnimGLMin = new ScaleAnimGL(1, 1, 1, 0.7f, 200);
        // ScaleAnimGL scaleAnimGLResume = new ScaleAnimGL(1, 1, 0.7f, 1f,
        // 2000);
        ScaleResumeAnimGL scaleResumeAnimGL = new ScaleResumeAnimGL(1, 1, 1, 0.7f, 100, 100);
        // ScaleAnimGL empty = new ScaleAnimGL(1, 1, 1, 1, 1000L);
        AnimGLSet animset = new AnimGLSet(translateAnim, scaleResumeAnimGL);// ,
                                                                            // scaleAnimGLResume,
                                                                            // empty);
        // animset.setRepeat(true);
        item.startAnim(animset);

        return item;
    }

    /**
     * �Զ���һ��Item ����ʵ��ÿ��item�Ķ���
     * 
     * @author michaelzuo
     */
    private class CustomerItem extends ItemModel {

        private float heightOffset;

        public CustomerItem(int latE6, int lonE6, IItemMarkerAdapter adapter, float offset) {
            super(latE6, lonE6, adapter);
            this.heightOffset = offset;
        }

        public CustomerItem(int latE6, int lonE6, float offset) {
            super(latE6, lonE6);
            this.heightOffset = offset;
        }

        @Override
        public float onGetItemScale(double distance, float angleScale) {
            /*
             * ��distance < minDis , scaleΪmaxScale ��distance > maxDis ,
             * scaleΪminScale ��distance��minDis��maxDis֮�� , scale�������仯 ��distance >
             * maxShowDis , ������ʾ
             */
            float scale = 1.0f;

            // ����ԶС����
            // final float minScale = 0.8f;
            // final float maxScale = 1.5f;
            // final double minDis = 20;
            // final double maxDis = 40;
            // final double maxShowDis = 150;
            //
            // if(distance < minDis) {
            // scale = maxScale;
            // } else if (distance < maxDis) {
            // scale = (float) (maxScale - (maxScale - minScale) * (distance -
            // minDis) / (maxDis - minDis));
            // } else if (distance < maxShowDis) {
            // scale = minScale;
            // } else {
            // scale = SCALE_INVISIBLE;
            // return scale;
            // }

            // �����ӽ�poi��������
            final float factor = 0.2f; // �����ӽǷŴ�����Item���ŵ�����
            scale = scale + (angleScale - 1) * factor;

            return scale;
        }

        @Override
        protected float onGetItemHeightOffset() {
            return this.heightOffset;
        }

    }

}
