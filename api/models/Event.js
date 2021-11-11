/**
 * Person.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {

    //  ╔═╗╦═╗╦╔╦╗╦╔╦╗╦╦  ╦╔═╗╔═╗
    //  ╠═╝╠╦╝║║║║║ ║ ║╚╗╔╝║╣ ╚═╗
    //  ╩  ╩╚═╩╩ ╩╩ ╩ ╩ ╚╝ ╚═╝╚═╝
    name: {
      type: "string"
    } , 

    short_desc: {
      type: 'string'
    } ,

   long_desc: {
    type: 'string'
    } ,

   img_url: {
       type: 'string'
    } ,

    organizer: {
      type: "string"

    },

    event_date: {
        type: 'string', columnType: 'date' 
    } ,

    start_time: {
      type: 'string', columnType: 'dateTime' 
   } ,

    end_time: {
      type: 'string', columnType: 'dateTime' 
    } ,
   
    venue: {
      type: "string"
    } ,

    quota: {
      type: "number"
    } ,
    
    Highlight: {
      type: "string"
    }
    ,
    consultants: {
      collection: 'User',
      via: 'clients'
    },

    

    //  ╔═╗╔╦╗╔╗ ╔═╗╔╦╗╔═╗
    //  ║╣ ║║║╠╩╗║╣  ║║╚═╗
    //  ╚═╝╩ ╩╚═╝╚═╝═╩╝╚═╝


    //  ╔═╗╔═╗╔═╗╔═╗╔═╗╦╔═╗╔╦╗╦╔═╗╔╗╔╔═╗
    //  ╠═╣╚═╗╚═╗║ ║║  ║╠═╣ ║ ║║ ║║║║╚═╗
    //  ╩ ╩╚═╝╚═╝╚═╝╚═╝╩╩ ╩ ╩ ╩╚═╝╝╚╝╚═╝

  },

};

