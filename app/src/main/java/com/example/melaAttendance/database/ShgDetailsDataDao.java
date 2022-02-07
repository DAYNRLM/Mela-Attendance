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
 * DAO for table "SHG_DETAILS_DATA".
*/
public class ShgDetailsDataDao extends AbstractDao<ShgDetailsData, Void> {

    public static final String TABLENAME = "SHG_DETAILS_DATA";

    /**
     * Properties of entity ShgDetailsData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Shg_code = new Property(0, String.class, "shg_code", false, "SHG_CODE");
        public final static Property User_name = new Property(1, String.class, "user_name", false, "USER_NAME");
        public final static Property Shg_reg_id = new Property(2, int.class, "shg_reg_id", false, "SHG_REG_ID");
        public final static Property Shg_main_participant = new Property(3, String.class, "shg_main_participant", false, "SHG_MAIN_PARTICIPANT");
        public final static Property Shg_reg_code = new Property(4, String.class, "shg_reg_code", false, "SHG_REG_CODE");
        public final static Property Mela_id = new Property(5, int.class, "mela_id", false, "MELA_ID");
        public final static Property Shggst = new Property(6, String.class, "shggst", false, "SHGGST");
        public final static Property Entity_code = new Property(7, String.class, "entity_code", false, "ENTITY_CODE");
        public final static Property User_id = new Property(8, int.class, "user_id", false, "USER_ID");
        public final static Property Shg_name = new Property(9, String.class, "shg_name", false, "SHG_NAME");
        public final static Property Main_participant_mobile = new Property(10, String.class, "main_participant_mobile", false, "MAIN_PARTICIPANT_MOBILE");
        public final static Property Participant_status = new Property(11, String.class, "participant_status", false, "PARTICIPANT_STATUS");
        public final static Property StallNo = new Property(12, String.class, "stallNo", false, "STALL_NO");
    }


    public ShgDetailsDataDao(DaoConfig config) {
        super(config);
    }
    
    public ShgDetailsDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHG_DETAILS_DATA\" (" + //
                "\"SHG_CODE\" TEXT," + // 0: shg_code
                "\"USER_NAME\" TEXT," + // 1: user_name
                "\"SHG_REG_ID\" INTEGER NOT NULL ," + // 2: shg_reg_id
                "\"SHG_MAIN_PARTICIPANT\" TEXT," + // 3: shg_main_participant
                "\"SHG_REG_CODE\" TEXT," + // 4: shg_reg_code
                "\"MELA_ID\" INTEGER NOT NULL ," + // 5: mela_id
                "\"SHGGST\" TEXT," + // 6: shggst
                "\"ENTITY_CODE\" TEXT," + // 7: entity_code
                "\"USER_ID\" INTEGER NOT NULL ," + // 8: user_id
                "\"SHG_NAME\" TEXT," + // 9: shg_name
                "\"MAIN_PARTICIPANT_MOBILE\" TEXT," + // 10: main_participant_mobile
                "\"PARTICIPANT_STATUS\" TEXT," + // 11: participant_status
                "\"STALL_NO\" TEXT);"); // 12: stallNo
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHG_DETAILS_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ShgDetailsData entity) {
        stmt.clearBindings();
 
        String shg_code = entity.getShg_code();
        if (shg_code != null) {
            stmt.bindString(1, shg_code);
        }
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(2, user_name);
        }
        stmt.bindLong(3, entity.getShg_reg_id());
 
        String shg_main_participant = entity.getShg_main_participant();
        if (shg_main_participant != null) {
            stmt.bindString(4, shg_main_participant);
        }
 
        String shg_reg_code = entity.getShg_reg_code();
        if (shg_reg_code != null) {
            stmt.bindString(5, shg_reg_code);
        }
        stmt.bindLong(6, entity.getMela_id());
 
        String shggst = entity.getShggst();
        if (shggst != null) {
            stmt.bindString(7, shggst);
        }
 
        String entity_code = entity.getEntity_code();
        if (entity_code != null) {
            stmt.bindString(8, entity_code);
        }
        stmt.bindLong(9, entity.getUser_id());
 
        String shg_name = entity.getShg_name();
        if (shg_name != null) {
            stmt.bindString(10, shg_name);
        }
 
        String main_participant_mobile = entity.getMain_participant_mobile();
        if (main_participant_mobile != null) {
            stmt.bindString(11, main_participant_mobile);
        }
 
        String participant_status = entity.getParticipant_status();
        if (participant_status != null) {
            stmt.bindString(12, participant_status);
        }
 
        String stallNo = entity.getStallNo();
        if (stallNo != null) {
            stmt.bindString(13, stallNo);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ShgDetailsData entity) {
        stmt.clearBindings();
 
        String shg_code = entity.getShg_code();
        if (shg_code != null) {
            stmt.bindString(1, shg_code);
        }
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(2, user_name);
        }
        stmt.bindLong(3, entity.getShg_reg_id());
 
        String shg_main_participant = entity.getShg_main_participant();
        if (shg_main_participant != null) {
            stmt.bindString(4, shg_main_participant);
        }
 
        String shg_reg_code = entity.getShg_reg_code();
        if (shg_reg_code != null) {
            stmt.bindString(5, shg_reg_code);
        }
        stmt.bindLong(6, entity.getMela_id());
 
        String shggst = entity.getShggst();
        if (shggst != null) {
            stmt.bindString(7, shggst);
        }
 
        String entity_code = entity.getEntity_code();
        if (entity_code != null) {
            stmt.bindString(8, entity_code);
        }
        stmt.bindLong(9, entity.getUser_id());
 
        String shg_name = entity.getShg_name();
        if (shg_name != null) {
            stmt.bindString(10, shg_name);
        }
 
        String main_participant_mobile = entity.getMain_participant_mobile();
        if (main_participant_mobile != null) {
            stmt.bindString(11, main_participant_mobile);
        }
 
        String participant_status = entity.getParticipant_status();
        if (participant_status != null) {
            stmt.bindString(12, participant_status);
        }
 
        String stallNo = entity.getStallNo();
        if (stallNo != null) {
            stmt.bindString(13, stallNo);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public ShgDetailsData readEntity(Cursor cursor, int offset) {
        ShgDetailsData entity = new ShgDetailsData( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // shg_code
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // user_name
            cursor.getInt(offset + 2), // shg_reg_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // shg_main_participant
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shg_reg_code
            cursor.getInt(offset + 5), // mela_id
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // shggst
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // entity_code
            cursor.getInt(offset + 8), // user_id
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // shg_name
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // main_participant_mobile
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // participant_status
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // stallNo
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ShgDetailsData entity, int offset) {
        entity.setShg_code(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUser_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setShg_reg_id(cursor.getInt(offset + 2));
        entity.setShg_main_participant(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShg_reg_code(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMela_id(cursor.getInt(offset + 5));
        entity.setShggst(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEntity_code(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUser_id(cursor.getInt(offset + 8));
        entity.setShg_name(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setMain_participant_mobile(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setParticipant_status(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setStallNo(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(ShgDetailsData entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(ShgDetailsData entity) {
        return null;
    }

    @Override
    public boolean hasKey(ShgDetailsData entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}