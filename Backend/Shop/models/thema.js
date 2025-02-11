module.exports = (sequelize, DataTypes) => {
    return sequelize.define('thema', {

        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true,
            allowNULL: false
        },
        
        thema_name: {
            type: DataTypes.STRING(40),
            allowNULL: false,
            unique: true
        },

        created_at: {
            type: DataTypes.DATE,
            allowNULL: true,
            defaultValue: sequelize.literal('now()')
        }

    },

        {
            timestamp: false,
            paranoid: true,
            underscored: true,
        }

    );

};
