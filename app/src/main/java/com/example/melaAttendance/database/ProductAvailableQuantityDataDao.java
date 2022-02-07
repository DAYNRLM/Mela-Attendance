package com.example.melaAttendance.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PRODUCT_AVAILABLE_QUANTITY_DATA".
*/
public class ProductAvailableQuantityDataDao extends AbstractDao<ProductAvailableQuantityData, Long> {

    public static final String TABLENAME = "PRODUCT_AVAILABLE_QUANTITY_DATA";

    /**
     * Properties of entity ProductAvailableQuantityData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Auto_gen_available_quantity = new Property(0, Long.class, "auto_gen_available_quantity", true, "_id");
        public final static Property Product_id = new Property(1, String.class, "product_id", false, "PRODUCT_ID");
        public final static Property Product_name = new Property(2, String.class, "product_name", false, "PRODUCT_NAME");
        public final static Property Available_quantity = new Property(3, String.class, "available_quantity", false, "AVAILABLE_QUANTITY");
    }


    public ProductAvailableQuantityDataDao(DaoConfig config) {
        super(config);
    }
    
    public ProductAvailableQuantityDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRODUCT_AVAILABLE_QUANTITY_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: auto_gen_available_quantity
                "\"PRODUCT_ID\" TEXT," + // 1: product_id
                "\"PRODUCT_NAME\" TEXT," + // 2: product_name
                "\"AVAILABLE_QUANTITY\" TEXT);"); // 3: available_quantity
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRODUCT_AVAILABLE_QUANTITY_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ProductAvailableQuantityData entity) {
        stmt.clearBindings();
 
        Long auto_gen_available_quantity = entity.getAuto_gen_available_quantity();
        if (auto_gen_available_quantity != null) {
            stmt.bindLong(1, auto_gen_available_quantity);
        }
 
        String product_id = entity.getProduct_id();
        if (product_id != null) {
            stmt.bindString(2, product_id);
        }
 
        String product_name = entity.getProduct_name();
        if (product_name != null) {
            stmt.bindString(3, product_name);
        }
 
        String available_quantity = entity.getAvailable_quantity();
        if (available_quantity != null) {
            stmt.bindString(4, available_quantity);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ProductAvailableQuantityData entity) {
        stmt.clearBindings();
 
        Long auto_gen_available_quantity = entity.getAuto_gen_available_quantity();
        if (auto_gen_available_quantity != null) {
            stmt.bindLong(1, auto_gen_available_quantity);
        }
 
        String product_id = entity.getProduct_id();
        if (product_id != null) {
            stmt.bindString(2, product_id);
        }
 
        String product_name = entity.getProduct_name();
        if (product_name != null) {
            stmt.bindString(3, product_name);
        }
 
        String available_quantity = entity.getAvailable_quantity();
        if (available_quantity != null) {
            stmt.bindString(4, available_quantity);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ProductAvailableQuantityData readEntity(Cursor cursor, int offset) {
        ProductAvailableQuantityData entity = new ProductAvailableQuantityData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // auto_gen_available_quantity
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // product_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // product_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // available_quantity
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ProductAvailableQuantityData entity, int offset) {
        entity.setAuto_gen_available_quantity(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProduct_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setProduct_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAvailable_quantity(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ProductAvailableQuantityData entity, long rowId) {
        entity.setAuto_gen_available_quantity(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ProductAvailableQuantityData entity) {
        if(entity != null) {
            return entity.getAuto_gen_available_quantity();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ProductAvailableQuantityData entity) {
        return entity.getAuto_gen_available_quantity() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
