const express = require('express')
const router = express.Router()
const bcrypt = require('bcryptjs');
const { v4: uuid } = require("uuid");

const faker = require('faker')
faker.locale = 'en_IND'

const Case = require('../models/Case');
const User = require('../models/User');
const Ministry = require('../models/Ministry');
const DailyStats = require('../models/DailyStats');

router.get('/', async (req, res)=>{
	var count = {
		users: await User.countDocuments({}),
		ministries: await Ministry.countDocuments({}),
		cases: await Case.countDocuments({}),
		resolved: await Case.countDocuments({status: 'resolved'}),
		submitted: await Case.countDocuments({status: 'submitted'}),
		archived: await Case.countDocuments({status: 'archived'}),
		accepted: await Case.countDocuments({status: 'accepted'}),
		stats: await DailyStats.countDocuments({})
	}

	res.render('faker', {count})
})

//delete all users documents
router.post('/users/dropall', (req, res)=>{
	User.deleteMany({})
	.then(done=>{
		console.log('Dropped all User documents')
		return res.redirect('/faker')
	})
})
//delete all cases documents
router.post('/cases/dropall', (req, res)=>{
	Case.deleteMany({})
	.then(done=>{
		console.log('Dropped all Case documents')
		return res.redirect('/faker')
	})
})

//delete all ministries documents
router.post('/ministry/dropall', (req, res)=>{
	Ministry.deleteMany({})
	.then(done=>{
		console.log('Dropped all Ministrys, and Cases documents')
		return res.redirect('/faker')
	})
})

//delete all daily stats documents
router.post('/stats/dropall', (req, res)=>{
	DailyStats.deleteMany({})
	.then(done=>{
		console.log('Dropped all daily stats documents')
		return res.redirect('/faker')
	})
})


//add random users
router.post('/users', async (req, res) => {
	let n = parseInt(req.body.number)

	for (let i=0; i<n; i++) {
		await new User({
			email: faker.internet.email()
		}).save()
		.catch(err=> err)
	}
	res.redirect('/faker')
})



//randomly set some cases to resolved status
router.post('/cases/setresolved', async (req, res)=>{
	cases = await Case.find({})

	let number = parseInt(req.body.number)
	if (number > cases.length) number = cases.length

	for(let i=0; i<number; i++) {
		let index = faker.datatype.number({ min: 0, max: cases.length-1 })
		let c = cases[index]
		await Case.findOneAndUpdate({_id: c.id}, {
			status: 'resolved',
			resolvedAt: faker.date.between( (c.date).toString(), (new Date()).toString() ),
		}, {new: true})
		.then(async (saved)=>{
			let currentDate = new Date(saved.date.toString()).setHours(00, 00, 00);
			let stats = await DailyStats.findOne({date: currentDate})
			if (!stats) {
				await new DailyStats({
					date: currentDate,
					received: 0, resolved: 1
				}).save()
			}
			else {
				await DailyStats.findOneAndUpdate({ date: currentDate }, { $inc: { resolved: 1 } }, { new: true })
			}
		})
	}

	res.redirect('/faker')
})

//randomly set status of some cases to different status
router.post('/cases/setstatus', async (req, res)=>{
	cases = await Case.find({})
	allowedStatuses = ["submitted", "archived", "accepted"]

	let number = parseInt(req.body.number)
	if (number > cases.length) number = cases.length

	for(let i=0; i<number; i++) {
		let index = faker.datatype.number({ min: 0, max: cases.length-1 })
		let c = cases[index]
		if (c.status !== 'resolved') {
			await Case.findOneAndUpdate({_id: c.id}, {
				status: allowedStatuses[faker.datatype.number({ min: 0, max: 2})]
			})
		}
	}

	res.redirect('/faker')
})



//temp
router.get('/cases/fixstatus', async (req, res)=>{
	cases = await Case.find({})
	cases.forEach(async (c)=>{
		if (c.status==='resolved' && !c.resolvedAt){
			await Case.findOneAndUpdate({_id: c.id}, {
				status: 'accepted'
			}).then(resp=> console.log('fixed a case: ', c.caseId))
		}
	})

	res.redirect('/faker')
})


//add new random Ministries
router.post('/ministry', async (req, res)=>{

	var allMinistries = [
		{
		"ministryId": "MOAAFW_O128WQ",
		"ministryName": "Ministry of Agriculture and Farmers Welfare",
		"departments": [
				'Department of Agricultural Research and Education (DARE)',
				'Department of Agriculture, Cooperation and Farmers Welfare',
				'Not sure'
			]
		},
		{
		"ministryId": "MOA_JBVM92",
		"ministryName": "Ministry of AYUSH",
		"departments": ['None']
		},
		{
		"ministryId": "MOCAF_IGQZSQ",
		"ministryName": "Ministry of Chemicals and Fertilizers",
		"departments": [
				'Department of Chemicals and Petrochemicals',
				'Department of Fertilizers',
				'Department of Pharmaceuticals',
				'Not sure'
			]
		},
		{
		"ministryId": "MOCA_KGMSI3",
		"ministryName": "Ministry of Civil Aviation",
		"departments": ['None']
		},
		{
		"ministryId": "MOC_V1IIL2",
		"ministryName": "Ministry of Coal",
		"departments": ['None']
		},
		{
		"ministryId": "MOCAI_S2ID0D",
		"ministryName": "Ministry of Commerce and Industry",
		"departments": [
				'Department for Promotion of Industry and Internal Trade',
				'Department of Commerce',
				'Not sure'
			]
		},
		{
		"ministryId": "MOC_9T0PJH",
		"ministryName": "Ministry of Communications",
		"departments": [
				'Department of Posts',
				'Department of Telecommunications (DOT)',
				'Not sure'
			]
		},
		{
		"ministryId": "MOCAFAPD_2XI2YN",
		"ministryName": "Ministry of Consumer Affairs, Food and Public Distribution",
		"departments": [
				'Department of Consumer Affairs',
				'Department of Food and Public Distribution',
				'Not sure'
			]
		},
		{
		"ministryId": "MOCA_AWOPJE",
		"ministryName": "Ministry of Corporate Affairs",
		"departments": ['None']
		},
		{
		"ministryId": "MOC_5HZABZ",
		"ministryName": "Ministry of Culture",
		"departments": ['None']
		},
		{
		"ministryId": "MOD_A5JW72",
		"ministryName": "Ministry of Defence",
		"departments": [
				'Department of Defence',
				'Department of Defence Production',
				'Department of Defence Research & Development',
				'Department of Ex-Servicemen Welfare',
				'Not sure'
			]
		},
		{
		"ministryId": "MFDONER_1UGYZ2",
		"ministryName": "Ministry for Development of North Eastern Region",
		"departments": ['None']
		},
		{
		"ministryId": "MOES_Q0U4N0",
		"ministryName": "Ministry of Earth Sciences",
		"departments": [
				'India Meteorological Department (IMD)',
				'Not sure'
			]
		},
		{
		"ministryId": "MOEAIT_5HMCXZ",
		"ministryName": "Ministry of Electronics and Information Technology",
		"departments": ['None']
		},
		{
		"ministryId": "MOEFACC_THEAQ1",
		"ministryName": "Ministry of Environment, Forests and Climate Change",
		"departments": ['None']
		},
		{
		"ministryId": "MOEA_HJ12ZJ",
		"ministryName": "Ministry of External Affairs",
		"departments": ['None']
		},
		{
		"ministryId": "MOF_6V5ZLF",
		"ministryName": "Ministry of Finance",
		"departments": [
				'Department of Economic Affairs',
				'Department of Expenditure',
				'Department of Financial Services',
				'Department of Investment and Public Asset Management',
				'Department of Revenue',
				'Not sure'
			]
		},
		{
		"ministryId": "MOFAHAD_574HHH",
		"ministryName": "Ministry of Fisheries, Animal Husbandry and Dairying",
		"departments": [
				'Department of Animal Husbandry and Dairying',
				'Department of Fisheries',
				'Not sure'
			]
		},
		{
		"ministryId": "MOFPI_RYWOYJ",
		"ministryName": "Ministry of Food Processing Industries",
		"departments": ['None']
		},
		{
		"ministryId": "MOHAFW_0TVW6I",
		"ministryName": "Ministry of Health and Family Welfare",
		"departments": [
				'Department of Health and Family Welfare',
				'Department of Health Research, Ministry of Health & Family Welfare',
				'Not sure'
			]
		},
		{
		"ministryId": "MOHIAPE_02QGNE",
		"ministryName": "Ministry of Heavy Industries and Public Enterprises",
		"departments": [
				'Department of Heavy Industry',
				'Department of Public Enterprises',
				'Not sure'
			]
		},
		{
		"ministryId": "MOHA_8I2K7V",
		"ministryName": "Ministry of Home Affairs",
		"departments": [
				'Central Armed Police Forces',
				'Central Police Ministryanisation',
				'Department of Border Management',
				'Department of Home',
				'Department of Internal Security',
				'Department of Jammu & Kashmir (J & K) Affairs',
				'Department of Official Language',
				'Department of States',
				'Not sure'
			]
		},
		{
		"ministryId": "MOHAUA_OFHZB9",
		"ministryName": "Ministry of Housing and Urban Affairs",
		"departments": ['None']
		},
		{
		"ministryId": "MOHRD_0NOST8",
		"ministryName": "Ministry of Education",
		"departments": [
				'Department of Higher Education',
				'Department of School Education and Literacy',
				'Not sure'
			]
		},
		{
		"ministryId": "MOIAB_6C9UQX",
		"ministryName": "Ministry of Information and Broadcasting",
		"departments": ['None']
		},
		{
		"ministryId": "MOJS_Z27HPH",
		"ministryName": "Ministry of Jal Shakti",
		"departments": [
				'Department of Drinking Water and Sanitation',
				'Department of Water Resources, River Development and Ganga Rejuvenation',
				'Not sure'
			]
		},
		{
		"ministryId": "MOLAE_D3LO7P",
		"ministryName": "Ministry of Labour and Employment",
		"departments": ['None']
		},
		{
		"ministryId": "MOLAJ_4L63KP",
		"ministryName": "Ministry of Law and Justice",
		"departments": [
				'Department of Justice',
				'Department of Legal Affairs',
				'Legislative Department',
				'Not sure'
			]
		},
		{
		"ministryId": "MOMSAME_9V8C16",
		"ministryName": "Ministry of Micro, Small and Medium Enterprises",
		"departments": ['None']
		},
		{
		"ministryId": "MOM_4B9B5F",
		"ministryName": "Ministry of Mines",
		"departments": ['None']
		},
		{
		"ministryId": "MOMA_2KKDY8",
		"ministryName": "Ministry of Minority Affairs",
		"departments": ['None']
		},
		{
		"ministryId": "MONARE_FF2CRD",
		"ministryName": "Ministry of New and Renewable Energy",
		"departments": ['None']
		},
		{
		"ministryId": "MOPR_LQIWSK",
		"ministryName": "Ministry of Panchayati Raj",
		"departments": ['None']
		},
		{
		"ministryId": "MOPA_4ALO98",
		"ministryName": "Ministry of Parliamentary Affairs",
		"departments": ['None']
		},
		{
		"ministryId": "MOPPGAP_AGR4FD",
		"ministryName": "Ministry of Personnel, Public Grievances and Pensions",
		"departments": [
				'Department of Administrative Reforms and Public Grievances (DARPG)',
				`Department of Pension & Pensioner's Welfare`,
				`Department of Personnel and Training`,
				'Not sure'
			]
		},
		{
		"ministryId": "MOPANG_PUOLJH",
		"ministryName": "Ministry of Petroleum and Natural Gas",
		"departments": ['None']
		},
		{
		"ministryId": "MOP_RG01AH",
		"ministryName": "Ministry of Planning",
		"departments": ['None']
		},
		{
		"ministryId": "MOP_9PMAJI",
		"ministryName": "Ministry of Power",
		"departments": ['None']
		},
		{
		"ministryId": "MOR_R3QHRO",
		"ministryName": "Ministry of Railways",
		"departments": ['None']
		},
		{
		"ministryId": "MORTAH_SPXJEF",
		"ministryName": "Ministry of Road Transport and Highways",
		"departments": ['None']
		},
		{
		"ministryId": "MORD_UC8N38",
		"ministryName": "Ministry of Rural Development",
		"departments": [
				'Department of Land Resources (DLR)',
				'Department of Rural Development (DRD)',
				'Not sure'
			]
		},
		{
		"ministryId": "MOSAT_5JPDHI",
		"ministryName": "Ministry of Science and Technology",
		"departments": [
				'Department of Biotechnology (DBT), Government of India',
				'Department of Science and Technology (DST)',
				'Department of Scientific and Industrial Research (DSIR)',
				'Not sure'
			]
		},
		{
		"ministryId": "MOS_0SYNYA",
		"ministryName": "Ministry of Shipping",
		"departments": ['None']
		},
		{
		"ministryId": "MOSDAE_9MRQMO",
		"ministryName": "Ministry of Skill Development and Entrepreneurship",
		"departments": ['None']
		},
		{
		"ministryId": "MOSJAE_WGFQ8K",
		"ministryName": "Ministry of Social Justice and Empowerment",
		"departments": [
				'Department of Empowerment of Persons with Disabilities',
				'Department of Social Justice and Empowerment',
				'Not sure'
			]
		},
		{
		"ministryId": "MOSAPI_M9V4G7",
		"ministryName": "Ministry of Statistics and Programme Implementation",
		"departments": ['None']
		},
		{
		"ministryId": "MOS_2SUE9Y",
		"ministryName": "Ministry of Steel",
		"departments": ['None']
		},
		{
		"ministryId": "MOT_O708U7",
		"ministryName": "Ministry of Textiles",
		"departments": ['None']
		},
		{
		"ministryId": "MOT_G06N1M",
		"ministryName": "Ministry of Tourism",
		"departments": ['None']
		},
		{
		"ministryId": "MOTA_Z9X782",
		"ministryName": "Ministry of Tribal Affairs",
		"departments": ['None']
		},
		{
		"ministryId": "MOWACD_TSVNRQ",
		"ministryName": "Ministry of Women and Child Development",
		"departments": ['None']
		},
		{
		"ministryId": "MOYAAS_A87KUG",
		"ministryName": "Ministry of Youth Affairs and Sports",
		"departments": [
				'Department of Sports',
				'Department of Youth Affairs',
				'Not sure'
			]
		},
		{
		"ministryId": "PMO_LSMM47",
		"ministryName": "Prime Minister's Office",
		"departments": [
				'Department of Atomic Energy',
				'Department of Space',
				'Not sure'
			]
		}
	]

	for (let i=0; i < allMinistries.length ; i++) {
		await new Ministry({
			ministryId: allMinistries[i].ministryId,
			ministryName: allMinistries[i].ministryName,
			departments: allMinistries[i].departments,
			password: await bcrypt.hash('password', 10),
			signUpDate: faker.date.between('March 14 2020 07:41:18 GMT+0530 (India Standard Time)', 'July 17 2020 07:41:18 GMT+0530 (India Standard Time)')
		}).save()
	}

	res.redirect('/faker')
});


//generate random cases
/*
generating cases depends on Ministries and users.
So generate both of them before generating cases
*/
router.post('/cases', async (req, res)=>{

	let n = parseInt(req.body.number)
	var users = await User.find({})
	var ministries = await Ministry.find({})

	for (let i=1; i<n+1 ; i++) {

		let caseId = generateCaseID();
		let user = users[faker.datatype.number({ min: 0, max: users.length-1 })]
		let mini = ministries[faker.datatype.number({ min: 0, max: ministries.length-1 })]
		let ministryId = mini.ministryId
		let department
		if (mini.departments.length) {
			department = mini.departments[faker.datatype.number({ min: 0, max: mini.departments.length-1 })]
		}
		else department = "None"
		

		var picsArray = []
		//get url of some random real images and push to picsArray
		for (let j=0; j<faker.datatype.number({ min: 2, max: 5 }); j++){
			picsArray.push(`http://lorempixel.com/800/480/`)
		}

		await new Case({
			caseId: caseId,
			userId: user.id,
			email: user.email,
			ministryId: ministryId,
			department: department,
			date: faker.date.between('March 14 2020 07:41:18 GMT+0530 (India Standard Time)', (new Date()).toString()),
			name: faker.company.companyName() + ' ' + faker.company.companySuffix(),
			place: faker.address.city(),
			coordinates: {
				type: "Point",
				//longi, lati
				coordinates: [faker.datatype.number({ min: 72.00, max: 85.00 }), faker.datatype.number({ min: 19.00, max: 28.00 })]
			},
			location: {
				address: faker.address.streetAddress(),
				pin: faker.address.zipCode(),
				district: faker.address.city()
			},
			description: faker.random.words(faker.datatype.number({ min: 80, max: 200 })),
			picsArray: picsArray,
			audiosArray: [],
			videosArray: [],
			agent: 'web'
		}).save()
		.then(async (caseReported)=>{
			//add this case to the user database (push to casesArray)
			await User.findOneAndUpdate({ _id: user.id}, { $push: {casesArray: caseId} })
			 // store into daily stats for chart purpose
			let currentDate = new Date(caseReported.date.toString()).setHours(00, 00, 00);
			let stats = await DailyStats.findOne({date: currentDate})
			if (!stats) {
				await new DailyStats({
					date: currentDate,
					received: 1, resolved: 0
				}).save()
			}
			else {
				await DailyStats.findOneAndUpdate({ date: currentDate }, { $inc: { received: 1 } }, {new: true })
			}

			console.log(`${i} cases saved, caseId: ${caseId}`)
			})
		.catch(err=>err)
	}
	res.redirect('/faker')
})


//function to check login
function isLoggedIn(req, res, next) {
	if(req.isAuthenticated() && req.user.adminId === 'admin') next()
	else res.redirect("/admin/login")
}

// fn to get a random unused case ID
const generateCaseID = () => {
	return uuid();
}

module.exports = router;