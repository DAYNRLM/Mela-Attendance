package com.example.melaAttendance.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.melaAttendance.database.AttendanceFlagData;
import com.example.melaAttendance.database.BillDetailsData;
import com.example.melaAttendance.database.ProductAvailableQuantityData;
import com.example.melaAttendance.database.SelectedProductDetails;
import com.example.melaAttendance.database.ShgDetailsData;
import com.example.melaAttendance.database.ShgMemberDetailsData;
import com.example.melaAttendance.database.ShgsDetailsOnLoginData;
import com.example.melaAttendance.database.SoldProductDetailsData;

import com.example.melaAttendance.database.AttendanceFlagDataDao;
import com.example.melaAttendance.database.BillDetailsDataDao;
import com.example.melaAttendance.database.ProductAvailableQuantityDataDao;
import com.example.melaAttendance.database.SelectedProductDetailsDao;
import com.example.melaAttendance.database.ShgDetailsDataDao;
import com.example.melaAttendance.database.ShgMemberDetailsDataDao;
import com.example.melaAttendance.database.ShgsDetailsOnLoginDataDao;
import com.example.melaAttendance.database.SoldProductDetailsDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig attendanceFlagDataDaoConfig;
    private final DaoConfig billDetailsDataDaoConfig;
    private final DaoConfig productAvailableQuantityDataDaoConfig;
    private final DaoConfig selectedProductDetailsDaoConfig;
    private final DaoConfig shgDetailsDataDaoConfig;
    private final DaoConfig shgMemberDetailsDataDaoConfig;
    private final DaoConfig shgsDetailsOnLoginDataDaoConfig;
    private final DaoConfig soldProductDetailsDataDaoConfig;

    private final AttendanceFlagDataDao attendanceFlagDataDao;
    private final BillDetailsDataDao billDetailsDataDao;
    private final ProductAvailableQuantityDataDao productAvailableQuantityDataDao;
    private final SelectedProductDetailsDao selectedProductDetailsDao;
    private final ShgDetailsDataDao shgDetailsDataDao;
    private final ShgMemberDetailsDataDao shgMemberDetailsDataDao;
    private final ShgsDetailsOnLoginDataDao shgsDetailsOnLoginDataDao;
    private final SoldProductDetailsDataDao soldProductDetailsDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        attendanceFlagDataDaoConfig = daoConfigMap.get(AttendanceFlagDataDao.class).clone();
        attendanceFlagDataDaoConfig.initIdentityScope(type);

        billDetailsDataDaoConfig = daoConfigMap.get(BillDetailsDataDao.class).clone();
        billDetailsDataDaoConfig.initIdentityScope(type);

        productAvailableQuantityDataDaoConfig = daoConfigMap.get(ProductAvailableQuantityDataDao.class).clone();
        productAvailableQuantityDataDaoConfig.initIdentityScope(type);

        selectedProductDetailsDaoConfig = daoConfigMap.get(SelectedProductDetailsDao.class).clone();
        selectedProductDetailsDaoConfig.initIdentityScope(type);

        shgDetailsDataDaoConfig = daoConfigMap.get(ShgDetailsDataDao.class).clone();
        shgDetailsDataDaoConfig.initIdentityScope(type);

        shgMemberDetailsDataDaoConfig = daoConfigMap.get(ShgMemberDetailsDataDao.class).clone();
        shgMemberDetailsDataDaoConfig.initIdentityScope(type);

        shgsDetailsOnLoginDataDaoConfig = daoConfigMap.get(ShgsDetailsOnLoginDataDao.class).clone();
        shgsDetailsOnLoginDataDaoConfig.initIdentityScope(type);

        soldProductDetailsDataDaoConfig = daoConfigMap.get(SoldProductDetailsDataDao.class).clone();
        soldProductDetailsDataDaoConfig.initIdentityScope(type);

        attendanceFlagDataDao = new AttendanceFlagDataDao(attendanceFlagDataDaoConfig, this);
        billDetailsDataDao = new BillDetailsDataDao(billDetailsDataDaoConfig, this);
        productAvailableQuantityDataDao = new ProductAvailableQuantityDataDao(productAvailableQuantityDataDaoConfig, this);
        selectedProductDetailsDao = new SelectedProductDetailsDao(selectedProductDetailsDaoConfig, this);
        shgDetailsDataDao = new ShgDetailsDataDao(shgDetailsDataDaoConfig, this);
        shgMemberDetailsDataDao = new ShgMemberDetailsDataDao(shgMemberDetailsDataDaoConfig, this);
        shgsDetailsOnLoginDataDao = new ShgsDetailsOnLoginDataDao(shgsDetailsOnLoginDataDaoConfig, this);
        soldProductDetailsDataDao = new SoldProductDetailsDataDao(soldProductDetailsDataDaoConfig, this);

        registerDao(AttendanceFlagData.class, attendanceFlagDataDao);
        registerDao(BillDetailsData.class, billDetailsDataDao);
        registerDao(ProductAvailableQuantityData.class, productAvailableQuantityDataDao);
        registerDao(SelectedProductDetails.class, selectedProductDetailsDao);
        registerDao(ShgDetailsData.class, shgDetailsDataDao);
        registerDao(ShgMemberDetailsData.class, shgMemberDetailsDataDao);
        registerDao(ShgsDetailsOnLoginData.class, shgsDetailsOnLoginDataDao);
        registerDao(SoldProductDetailsData.class, soldProductDetailsDataDao);
    }
    
    public void clear() {
        attendanceFlagDataDaoConfig.clearIdentityScope();
        billDetailsDataDaoConfig.clearIdentityScope();
        productAvailableQuantityDataDaoConfig.clearIdentityScope();
        selectedProductDetailsDaoConfig.clearIdentityScope();
        shgDetailsDataDaoConfig.clearIdentityScope();
        shgMemberDetailsDataDaoConfig.clearIdentityScope();
        shgsDetailsOnLoginDataDaoConfig.clearIdentityScope();
        soldProductDetailsDataDaoConfig.clearIdentityScope();
    }

    public AttendanceFlagDataDao getAttendanceFlagDataDao() {
        return attendanceFlagDataDao;
    }

    public BillDetailsDataDao getBillDetailsDataDao() {
        return billDetailsDataDao;
    }

    public ProductAvailableQuantityDataDao getProductAvailableQuantityDataDao() {
        return productAvailableQuantityDataDao;
    }

    public SelectedProductDetailsDao getSelectedProductDetailsDao() {
        return selectedProductDetailsDao;
    }

    public ShgDetailsDataDao getShgDetailsDataDao() {
        return shgDetailsDataDao;
    }

    public ShgMemberDetailsDataDao getShgMemberDetailsDataDao() {
        return shgMemberDetailsDataDao;
    }

    public ShgsDetailsOnLoginDataDao getShgsDetailsOnLoginDataDao() {
        return shgsDetailsOnLoginDataDao;
    }

    public SoldProductDetailsDataDao getSoldProductDetailsDataDao() {
        return soldProductDetailsDataDao;
    }

}
