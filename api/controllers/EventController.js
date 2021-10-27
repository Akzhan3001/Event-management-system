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
    // action - home
    home: async function (req, res) {
      var lim =4;
        var Highlights= await Event.find({
            where: { Highlight: 'on' },
            limit: lim,
            sort: 'createdAt'
        });


        return res.view('event/home', { events: Highlights });
    },

    // search function
    search: async function (req, res) {

        var whereClause = {};

        if (req.query.name) whereClause.name = { contains: req.query.name };
        if (req.query.organizer) whereClause.organizer = { contains: req.query.organizer.replace(/\+/g, ' ') };
        if (req.query.venue) whereClause.venue = { contains: req.query.venue.replace(/\+/g, ' ') };

        if (!whereClause) {
            return res.notFound('Could not find any form.');
        }
        start_date = new Date(req.query.start_date);

        end_date = new Date(req.query.end_date);


        var Events = await Event.find({
            where: whereClause,
            sort: 'id',
        });
        const numOfItemsPerPage = 2;
        var limit = Math.max(req.query.limit, 2) || 2;
        var offset = Math.max(req.query.offset, 0) || 0;
        var thoseEvents = await Event.find({
            where: whereClause,
            sort: 'id',
            limit: limit,
            skip: offset
        });
        var page_number = Math.ceil(await Events.length / 1);
        console.log(_.keys(whereClause.Data).length);
        // var count = await whereClause.length;

        return (res.view('event/search', { events: thoseEvents, numOfRecords: page_number }));
    },
    // action - paginate
    
   

};

