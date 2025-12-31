<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container py-5">
	<div class="d-flex justify-content-between align-items-center mb-4">
		<h2 class="mb-0">Thống kê Điểm danh</h2>
		<div>
			<a href="<c:url value='/classrooms/${classroomId}/detail'/>" class="btn btn-outline-secondary me-2">
				<i class="bi bi-arrow-left"></i> Quay lại
			</a>
			<a href="<c:url value='/attend/classrooms/${classroomId}/export'/>" class="btn btn-success">
				<i class="bi bi-file-earmark-excel"></i> Xuất Excel
			</a>
		</div>
	</div>

	<!-- Loading State -->
	<div id="loading" class="text-center py-5">
		<div class="spinner-border text-primary" role="status">
			<span class="visually-hidden">Loading...</span>
		</div>
	</div>

	<!-- Dashboard Content -->
	<div id="dashboard" class="d-none">
		<!-- Summary Cards -->
		<div class="row g-4 mb-4">
		    <div class="col-md-3">
		        <div class="card bg-primary text-white h-100 shadow-sm border-0">
		            <div class="card-body">
		                <h6 class="card-title text-uppercase mb-2 opacity-75">Tổng buổi học</h6>
		                <h2 class="display-6 fw-bold" id="totalSessions">0</h2>
		            </div>
		        </div>
		    </div>
		    
		    <div class="col-md-3">
		        <div class="card bg-info text-white h-100 shadow-sm border-0">
		            <div class="card-body">
		                <h6 class="card-title text-uppercase mb-2 opacity-75">Sĩ số lớp</h6>
		                <h2 class="display-6 fw-bold" id="totalStudents">0</h2>
		            </div>
		        </div>
		    </div>
		</div>

		<!-- Charts Row -->
		<div class="row g-4 mb-4">
			<div class="col-md-6">
				<div class="card h-100 shadow-sm">
					<div class="card-header">
						<h5 class="card-title mb-0">Tỷ lệ Chuyên cần (Toàn lớp)</h5>
					</div>
					<div class="card-body">
						<canvas id="attendancePieChart"></canvas>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="card h-100 shadow-sm">
					<div class="card-header">
						<h5 class="card-title mb-0">Top Vắng Nhiều Nhất</h5>
					</div>
					<div class="card-body">
						<canvas id="absentBarChart"></canvas>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Detailed Table -->
		<div class="card shadow-sm">
			<div class="card-header">
				<h5 class="card-title mb-0">Chi tiết từng sinh viên</h5>
			</div>
			<div class="table-responsive p-3">
				<table class="table table-striped" id="table">
					<thead>
						<tr>
							<th>Họ tên</th>
							<th class="text-center">Có mặt</th>
							<th class="text-center">Đi muộn</th>
							<th class="text-center">Vắng</th>
							<th class="text-center">Tỷ lệ (%)</th>
						</tr>
					</thead>
					<tbody>
						<!-- Rows injected by JS -->
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function() {
	const classroomId = "${classroomId}";
	
	fetch('${pageContext.request.contextPath}/attend/classrooms/' + classroomId + '/stats')
		.then(response => response.json())
		.then(data => {
			document.getElementById('loading').classList.add('d-none');
			document.getElementById('dashboard').classList.remove('d-none');
			
			// Update Summary
			document.getElementById('totalSessions').innerText = data.totalSessions;
			document.getElementById('totalStudents').innerText = data.studentStats.length;
			
			// 1. Pie Chart (Overall Status)
			const ctxPie = document.getElementById('attendancePieChart').getContext('2d');
			new Chart(ctxPie, {
				type: 'doughnut',
				data: {
					labels: ['Có mặt', 'Đi muộn', 'Vắng'],
					datasets: [{
						data: [
							data.overallDidAttend.PRESENT || 0,
							data.overallDidAttend.LATE || 0,
							data.overallDidAttend.ABSENT || 0
						],
						backgroundColor: ['#198754', '#ffc107', '#dc3545'],
					}]
				}
			});
			
			// 2. Bar Chart (Top Absentees)
			// Sort stats by absent count desc and take top 10
			const sortedStats = [...data.studentStats]
				.sort((a, b) => b.absentCount - a.absentCount)
				.slice(0, 10);
				
			const ctxBar = document.getElementById('absentBarChart').getContext('2d');
			new Chart(ctxBar, {
				type: 'bar',
				data: {
					labels: sortedStats.map(s => s.fullName || s.username),
					datasets: [{
						label: 'Số buổi vắng',
						data: sortedStats.map(s => s.absentCount),
						backgroundColor: '#dc3545',
					}]
				},
				options: {
					scales: {
						y: { beginAtZero: true, ticks: { stepSize: 1 } }
					}
				}
			});
			
			// 3. Populate Table
			const tbody = document.querySelector('#table tbody');
			data.studentStats.forEach(s => {
				const row = `
					<tr>
						<td><span class="fw-bold">\${s.fullName}</span> <br><small class="text-muted">\${s.username}</small></td>
						<td class="text-center text-success fw-bold">\${s.presentCount}</td>
						<td class="text-center text-warning fw-bold">\${s.lateCount}</td>
						<td class="text-center text-danger fw-bold">\${s.absentCount}</td>
						<td class="text-center">
							<div class="progress" style="height: 20px;">
								<div class="progress-bar \${getProgressBarClass(s.attendanceRate)}" role="progressbar" style="width: \${s.attendanceRate}%">
									\${s.attendanceRate}%
								</div>
							</div>
						</td>
					</tr>
				`;
				tbody.innerHTML += row;
			});
		})
		.catch(err => {
			console.error(err);
			alert('Lỗi tải dữ liệu thống kê');
		});
});

function getProgressBarClass(rate) {
	if (rate >= 80) return 'bg-success';
	if (rate >= 50) return 'bg-warning text-dark';
	return 'bg-danger';
}
</script>
