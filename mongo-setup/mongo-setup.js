rsconf = {
    _id: "rsmongo",
    members: [
        {
            "_id": 0,
            "host": "mongodb-primary:27017",
            "priority": 2
        },
        {
            "_id": 1,
            "host": "mongodb-secondary1:27017",
            "priority": 1
        },
        {
            "_id": 2,
            "host": "mongodb-secondary2:27017",
            "priority": 1
        }
    ]
}

rs.initiate(rsconf);