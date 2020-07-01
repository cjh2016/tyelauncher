package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.io.Serializable;
import java.util.List;

public class CatalogBean extends AbstractExpandableItem<CatalogBean> implements MultiItemEntity, Serializable {
    private String code;
    private List<CatalogBean> courses;
    private String name;
    private CatalogBean parent;
    private int rotation;

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation2) {
        this.rotation = rotation2;
    }

    public List<CatalogBean> getCourses() {
        return this.courses;
    }

    public void setCourses(List<CatalogBean> courses2) {
        this.courses = courses2;
    }

    public CatalogBean getParent() {
        return this.parent;
    }

    public void setParent(CatalogBean parent2) {
        this.parent = parent2;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getCode() {
        if (this.parent != null) {
            return this.parent.getCode().equals("") ? this.code : this.parent.getCode() + "_" + this.code;
        }
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getLevel() {
        return calLevel();
    }

    private int calLevel() {
        if (getParent() == null) {
            return 1;
        }
        if (getParent().getParent() == null) {
            return 2;
        }
        if (getParent().getParent().getParent() == null) {
            return 3;
        }
        return 4;
    }

    public int getItemType() {
        return calLevel();
    }

    public List<CatalogBean> getSubItems() {
        return getCourses();
    }

    /* Debug info: failed to restart local var, previous not found, register: 4 */
    public CatalogBean contain(CatalogBean bean) {
        if (bean.getName().equals(getName())) {
            return this;
        }
        for (int i = 0; i < this.courses.size(); i++) {
            CatalogBean b = this.courses.get(i).contain(bean);
            if (b != null) {
                return b;
            }
        }
        return null;
    }

    /* Debug info: failed to restart local var, previous not found, register: 3 */
    public CatalogBean contain(String typeName) {
        if (typeName.equals(getName())) {
            return this;
        }
        for (int i = 0; i < this.courses.size(); i++) {
            CatalogBean b = this.courses.get(i).contain(typeName);
            if (b != null) {
                return b;
            }
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogBean that = (CatalogBean) o;
        if (this.name != null) {
            return this.name.equals(that.name);
        }
        if (that.name != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.name != null) {
            return this.name.hashCode();
        }
        return 0;
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    public CatalogBean getTopLevelParent() {
        if (getParent() == null) {
            return this;
        }
        if (getParent().getParent() == null) {
            return getParent();
        }
        if (getParent().getParent().getParent() == null) {
            return getParent().getParent();
        }
        return getParent().getParent().getParent();
    }
}
