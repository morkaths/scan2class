<section class="row">
	<div class="col-12">
		<div class="row">
			<div class="col-6 col-lg-3 col-md-6">
				<div class="card">
					<div class="card-body px-4 py-4-5">
						<div class="row">
							<div
								class="col-md-4 col-lg-12 col-xl-12 col-xxl-5 d-flex justify-content-start ">
								<div class="stats-icon purple mb-2">
									<i class="iconly-boldProfile"></i>
								</div>
							</div>
							<div class="col-md-8 col-lg-12 col-xl-12 col-xxl-7">
								<h6 class="text-muted font-semibold">Total Users</h6>
								<h6 class="font-extrabold mb-0">${totalUsers}</h6>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-6 col-lg-3 col-md-6">
				<div class="card">
					<div class="card-body px-4 py-4-5">
						<div class="row">
							<div
								class="col-md-4 col-lg-12 col-xl-12 col-xxl-5 d-flex justify-content-start ">
								<div class="stats-icon blue mb-2">
									<i class="iconly-boldShow"></i>
								</div>
							</div>
							<div class="col-md-8 col-lg-12 col-xl-12 col-xxl-7">
								<h6 class="text-muted font-semibold">Classrooms</h6>
								<h6 class="font-extrabold mb-0">${totalClassrooms}</h6>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-6 col-lg-3 col-md-6">
				<div class="card">
					<div class="card-body px-4 py-4-5">
						<div class="row">
							<div
								class="col-md-4 col-lg-12 col-xl-12 col-xxl-5 d-flex justify-content-start ">
								<div class="stats-icon green mb-2">
									<i class="iconly-boldTime-Circle"></i>
								</div>
							</div>
							<div class="col-md-8 col-lg-12 col-xl-12 col-xxl-7">
								<h6 class="text-muted font-semibold">Sessions</h6>
								<h6 class="font-extrabold mb-0">${totalSessions}</h6>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-6 col-lg-3 col-md-6">
				<div class="card">
					<div class="card-body px-4 py-4-5">
						<div class="row">
							<div
								class="col-md-4 col-lg-12 col-xl-12 col-xxl-5 d-flex justify-content-start ">
								<div class="stats-icon red mb-2">
									<i class="iconly-boldLock"></i>
								</div>
							</div>
							<div class="col-md-8 col-lg-12 col-xl-12 col-xxl-7">
								<h6 class="text-muted font-semibold">Roles</h6>
								<h6 class="font-extrabold mb-0">${totalRoles}</h6>
							</div>
						</div>
					</div>
				</div>
			</div>
		<div class="row">
            <div class="col-12 col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4>Session Activity (Last 7 Days)</h4>
                    </div>
                    <div class="card-body">
                        <div id="sessions-chart"></div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h4>Users by Role</h4>
                    </div>
                    <div class="card-body">
                        <div id="roles-chart"></div>
                    </div>
                </div>
            </div>
        </div>
	</div>
</section>

<script src="/assets/extensions/apexcharts/apexcharts.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Sessions Chart
        var sessionsOptions = {
            series: [{
                name: "Sessions",
                data: JSON.parse('${sessionsChartData}')
            }],
            chart: {
                height: 350,
                type: 'line',
                zoom: {
                    enabled: false
                }
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                curve: 'straight'
            },
            title: {
                text: 'Sessions Created',
                align: 'left'
            },
            grid: {
                row: {
                    colors: ['#f3f3f3', 'transparent'],
                    opacity: 0.5
                },
            },
            xaxis: {
                categories: JSON.parse('${sessionsChartLabels}'),
            }
        };

        var sessionsChart = new ApexCharts(document.querySelector("#sessions-chart"), sessionsOptions);
        sessionsChart.render();

        // Roles Chart
        var rolesOptions = {
            series: JSON.parse('${rolesChartData}'),
            chart: {
                width: 380,
                type: 'pie',
            },
            labels: JSON.parse('${rolesChartLabels}'),
            responsive: [{
                breakpoint: 480,
                options: {
                    chart: {
                        width: 200
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }]
        };

        var rolesChart = new ApexCharts(document.querySelector("#roles-chart"), rolesOptions);
        rolesChart.render();
    });
</script>