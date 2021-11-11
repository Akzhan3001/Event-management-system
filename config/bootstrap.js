/**
 * Seed Function
 * (sails.config.bootstrap)
 *
 * A function that runs just before your Sails app gets lifted.
 * > Need more flexibility?  You can also create a hook.
 *
 * For more information on seeding your app with fake data, check out:
 * https://sailsjs.com/config/bootstrap
 */

module.exports.bootstrap = async function() {
 
  
  if (await Event.count() > 0) {
    return generateUsers();
  }

  await Event.createEach([
    
     
      {
          createdAt: 1635201476665,
          updatedAt: 1635201476665,
          id: 1,
          name: "Symphonia V",
          short_desc: "Music",
          long_desc: "Symphonia V",
          img_url: "https://images.macrumors.com/t/vMbr05RQ60tz7V_zS5UEO9SbGR0=/1600x900/smart/article-new/2018/05/apple-music-note.jpg",
          organizer: "Music Society",
          event_date: "2021-10-28",
          start_time: "10:00",
          end_time: "11:00",
          venue: "Old Campus",
          quota: 40,
          Highlight: "on"
        },
        {
          createdAt: 1635201690575,
          updatedAt: 1635201690575,
          id: 2,
          name: "Mafia",
          short_desc: "Mafia game",
          long_desc: "Mafia game, card games, snacks",
          img_url: "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Video-Game-Controller-Icon-IDV-green.svg/2048px-Video-Game-Controller-Icon-IDV-green.svg.png",
          organizer: "Student Union",
          event_date: "2021-10-29",
          start_time: "11:00",
          end_time: "12:00",
          venue: "WLB302",
          quota: 50,
          Highlight: ""
        },
        {
          createdAt: 1635202051632,
          updatedAt: 1635202051632,
          id: 3,
          name: "Career Talk for IT students",
          short_desc: "Career",
          long_desc: "Career consultation for graduates",
          img_url: "https://akm-img-a-in.tosshub.com/indiatoday/images/story/201707/it-career-tips-647_070517041447.jpg?size=1200:675",
          organizer: "Department of Computer Science",
          event_date: "2021-10-30",
          start_time: "09:00",
          end_time: "11:30",
          venue: "Old Campus",
          quota: 100,
          Highlight: "on"
        },
        {
          createdAt: 1635209140408,
          updatedAt: 1635209140408,
          id: 4,
          name: "I am Singer",
          short_desc: "contest",
          long_desc: "Singer contest",
          img_url: "https://pbs.twimg.com/profile_images/1055810870268313600/i5ecJMB9.jpg",
          organizer: "Student Union",
          event_date: "2021-11-01",
          start_time: "19:00",
          end_time: "20:30",
          venue: "DLB205",
          quota: 30,
          Highlight: "on"
        },

        {
          createdAt: 1635209517662,
          updatedAt: 1635209517662,
          id: 5,
          name: "Testimonial policy",
          short_desc: "testimonial",
          long_desc: "Testimonial policy",
          img_url: "https://busrpg.hkbu.edu.hk/wp-content/uploads/2019/10/about_hkbu_rpg.jpg",
          organizer: "Registry",
          event_date: "2021-11-02",
          start_time: "20:00",
          end_time: "21:30",
          venue: "AAB702",
          quota: 10,
          Highlight: ""
        },
      
        {
          createdAt: 1635298947374,
          updatedAt: 1635298947374,
          id: 6,
          name: "Hiking with CAS",
          short_desc: "Hiking with CAS",
          long_desc: "Hiking with Central Asian Society",
          img_url: "https://www.discoverhongkong.com/in/explore/great-outdoor/practical-hiking-tips-to-explore-hong-kongs-trails.thumb.800.480.png?ck=1595746678",
          organizer: "Central Asian Society",
          event_date: "2021-11-10",
          start_time: "08:00",
          end_time: "10:30",
          venue: "WLB302",
          quota: 40,
          Highlight: ""
        },

        {
          createdAt: 1635302436567,
          updatedAt: 1635302436567,
          id: 7,
          name: "Bussiness merkting training",
          short_desc: "Bussiness merkting training",
          long_desc: "Bussiness merkting training",
          img_url: "https://www.esrichina.hk/content/dam/distributor-share/esrichina-hk/images/corporate-training.jpg",
          organizer: "Career Office",
          event_date: "2021-11-06",
          start_time: "16:00",
          end_time: "17:30",
          venue: "Library",
          quota: 40,
          Highlight: "on"
        },



      ,
      
     
      // etc.
  ]);

  return generateUsers();
  
  async function generateUsers() {
  
   var hashedPassword = await sails.helpers.passwords.hashPassword('123456');

   await User.createEach([
      { username: "admin", password: hashedPassword, role:'admin' },
      { username: "student", password: hashedPassword, role: 'student' }
      // etc.
    ]);
  }
   
    
    
  
  
  
 
  // By convention, this is a good place to set up fake data during development.
  //
  // For example:
  // ```
  // // Set up fake development data (or if we already have some, avast)
  // if (await User.count() > 0) {
  //   return;
  // }
  //
  // await User.createEach([
  //   { emailAddress: 'ry@example.com', fullName: 'Ryan Dahl', },
  //   { emailAddress: 'rachael@example.com', fullName: 'Rachael Shaw', },
  //   // etc.
  // ]);
  // ```

};
