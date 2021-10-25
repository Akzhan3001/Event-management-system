/**
 * EventController
 *
 * @description :: Server-side actions for handling incoming requests.
 * @help        :: See https://sailsjs.com/docs/concepts/actions
 */

module.exports = {
    // action - create
    create: async function (req, res) {

        if (req.method == "GET") return res.view('event/create');
        
        var event = await Event.create(req.body).fetch();
        
        return res.redirect("/event/list");
    },
    // action - jsjson function
    json: async function (req, res) {

        var everyones = await Event.find();

        return res.json(everyones);
    },
    // action - list
    list: async function (req, res) {

        var everyones = await Event.find();
        
        return res.view('event/list', { events: everyones });
    },
    // action - list
    list: async function (req, res) {

        var everyones = await Event.find();
        
        return res.view('event/list', { events: everyones });
    },
    // action - read
    read: async function (req, res) {

        var thatEvent = await Event.findOne(req.params.id);

        if (!thatEvent) return res.notFound();

        return res.view('event/read', { event: thatEvent });
    },

    // action - delete 
    delete: async function (req, res) {

        var deletedEvent = await Event.destroyOne(req.params.id);

        if (!deletedEvent) return res.notFound();

        return res.redirect("/event/list");
    },
    // action - update
    update: async function (req, res) {

        if (req.method == "GET") {

            var thatEvent = await Event.findOne(req.params.id);

            if (!thatEvent) return res.notFound();

            return res.view('event/update', { event: thatEvent });
            
        } else {
        
            var updatedEvent = await Event.updateOne(req.params.id).set(req.body);

            if (!updatedEvent) return res.notFound();

            return res.redirect("/event/list");
        }
    },
        // search function
    search: async function (req, res) {
        
        var whereClause = {};
        
        if (req.query.name) whereClause.name = { contains: req.query.name };
        
        var parsedAge = parseInt(req.query.age);
        if (!isNaN(parsedAge)) whereClause.age = parsedAge;
        
        var thoseEvents = await Event.find({
            where: whereClause,
            sort: 'name'
        });
        
        return res.view('event/search', { events: thoseEvents });
    },  
            // action - paginate
    paginate: async function (req, res) {

        var limit = Math.max(req.query.limit, 2) || 2;
        var offset = Math.max(req.query.offset, 0) || 0;

        var someEvents = await Event.find({
            limit: limit,
            skip: offset
        });

        var count = await Event.count();

        return res.view('event/paginate', { events: someEvents, numOfRecords: count });
    },


};

