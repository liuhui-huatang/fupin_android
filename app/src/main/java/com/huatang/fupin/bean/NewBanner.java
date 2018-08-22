package com.huatang.fupin.bean;



import java.io.Serializable;
import java.util.List;

public class NewBanner implements Serializable {

    private List<String> model;
    private List<BannerColumn> shuffling_img;

    public List<String> getModel() {
        return model;
    }

    public void setModel(List<String> model) {
        this.model = model;
    }

    public List<BannerColumn> getShuffling_img() {
        return shuffling_img;
    }

    public void setShuffling_img(List<BannerColumn> shuffling_img) {
        this.shuffling_img = shuffling_img;
    }

    public  class BannerColumn implements Serializable{
        public String id;
        public String type;
        public String title;
        public String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
