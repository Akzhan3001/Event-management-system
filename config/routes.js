/**
 * Route Mappings
 * (sails.config.routes)
 *
 * Your routes tell Sails what to do each time it receives a request.
 *
 * For more information on configuring custom routes, check out:
 * https://sailsjs.com/anatomy/config/routes-js
 */

module.exports.routes = {

  /***************************************************************************
  *                                                                          *
  * Make the view located at `views/homepage.ejs` your home page.            *
  *                                                                          *
  * (Alternatively, remove this and add an `index.html` file in your         *
  * `assets` directory)                                                      *
  *                                                                          *
  ***************************************************************************/

  '/': { view: 'pages/homepage' },


  /***************************************************************************
  *                                                                          *
  * More custom routes here...                                               *
  * (See https://sailsjs.com/config/routes for examples.)                    *
  *                                                                          *
  * If a request to a URL doesn't match any of the routes in this file, it   *
  * is matched against "shadow routes" (e.g. blueprint routes).  If it does  *
  * not match any of those, it is matched against static assets.             *
  *                                                                          *
  ***************************************************************************/


   'GET /event/create': 'EventController.create',
   'POST /event/create': 'EventController.create',
   'GET /': 'EventController.list',
   'GET /event':  'EventController.list',
   'GET /event/list': 'EventController.list',
   'GET /event/json': 'EventController.json',
   'GET /event/read/:id': 'EventController.read',
   'POST /event/delete/:id': 'EventController.delete',
   'GET /event/update/:id': 'EventController.update',
   'POST /event/update/:id': 'EventController.update',
   'GET /event/search': 'EventController.search',
   'GET /event/paginate': 'EventController.paginate',
};
