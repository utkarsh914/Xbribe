const Case = require('../models/Case');
//helper fn
const getMinistries = require("./getMinistries");



const filterPost = async (type, req, res) => {
	let path = '/';
	if (type === "public") path = 'public/public-dashboard';
	else if (type === "admin") path = 'admin/cases-list';
	else if (type === "ministry") path = 'ministry/ministry-dashboard';

	const response = {
		"error": true, "message" : "Error fetching data",
		"pageNo": 1, "totalPages": 1,
		"posts": []
	};

	const filter = {
		$and: []
	};

	let {
		query,
		ministryId,
		status,
		from, to,
		pageNo, size, orderBy
	} = req.query;

	try {
		//if seeing on admin spam page
		// don't include spam cases by default
		if (req.url.split('?')[0] === '/spam') {
			filter['$and'].push({ spam: true });
			path = 'admin/spam-cases-list';
		}
		else {
			filter['$and'].push({ spam: { $ne: true } });
		}
	
		// only pass accepted or further status' cases to ministry
		if (type === 'ministry') {
			filter['$and'].push({ status: { $ne: 'submitted' } });
		}
	
		//make an object containing all ministries key value as id and name
		if (type !== "ministry") {
			response.ministries = await getMinistries();
		}
	
		const searchQuery = query; //|| req.signedCookies.query
		if (searchQuery) {
			const regex = new RegExp(searchQuery, 'i');
			const s =	[
				{ 'location.address': regex },
				{ 'location.pin': regex },
				{ 'location.district': regex },
				{ caseId: regex },
				{ email: regex },
				{ ministryId: regex },
				{ department: regex },
				{ name: regex },
				{ place: regex }
			];
			filter["$and"].push({ '$or': s });
		}
		
		// if it is for ministry portal, show only cases of that ministry
		if (type === "ministry") {
			filter['$and'].push({ ministryId: req.user.ministryId });
		}
		// if for other portal, and ministry ID has been specified
		else if (ministryId && ministryId !== "any") {
			filter['$and'].push({ ministryId: ministryId });
		}
		
		// if some status has been specified
		if (status && status !== "any") {
			filter['$and'].push({ status: status });
		}
	
		const datefilter = {
			"$gte": (from) ? 
				new Date(new Date(from).setHours(00, 00, 00)) :
				new Date(new Date('1970-01-01').setHours(00, 00, 00)),
			"$lt": (to) ?
				new Date(new Date(to).setHours(23, 59, 59)) :
				new Date()
		};
		// if date to < date from
		if (datefilter["$lt"] < datefilter["$gte"]) {
			response.message = "Date(From) can't be greater than Date(to)";
			return res.render(path, response);
		}
	
		filter['$and'].push({ date: datefilter });
	
		pageNo = parseInt(pageNo) || 1;
		if (pageNo < 1) pageNo = 1;
		size = parseInt(size) || 20;
		orderBy = orderBy || "new";
		let totalPages = 1;
	
		// console.log(filter)
		const count = await Case.countDocuments(filter);
		console.log(filter);
	
		if (count === 0) {
			response.error = true;
			response.message = "No records found for this query!";
			const reminders = await Case
				.find({ resolvedAt: null, remindedAt: { $ne: null }, spam: { $ne: true } })
				.sort({ remindedAt: -1 })
				.limit(10);
			response.reminders = reminders;

			console.log(path);
			console.log(response);

			return res.render(path, response);
		}
			
		totalPages = Math.ceil(count / size);
	
		const data = await Case.find(filter)
			.sort({ date: orderBy === "new" ? -1 : 1 })
			.skip(size * (pageNo - 1))
			.limit(size);
	
		//query to send to db for pagination and all other rendering
		response.error = false;
		if (searchQuery) {
			response.message = `Found ${count} results for "${searchQuery}"`;
		}else {
			response.message = `Successfully fetched ${count} results in ${totalPages} pages`;
		}
		response.pageNo = pageNo;
		response.pageSize = size;
		response.totalPages = totalPages;
		response.posts = data;
	
		if (type === "admin") {
			// find cases which are reminded but not resolved yet
			const reminders = await Case
				.find({ resolvedAt: null, remindedAt: { $ne: null }, spam: { $ne: true } })
				.sort({ remindedAt: -1 })
				.limit(10);
			response.reminders = reminders;
			res.render(path, response);
		}
		else {
			console.log('Not admin');
			res.render(path, response);
		}
	}

	catch (err) {
		console.log("Error occured:\n", err);
		res.status(500).send("Some error occured on server side.");
	}
}



module.exports = filterPost;