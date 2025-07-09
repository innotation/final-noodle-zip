package noodlezip.report.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.report.dto.ReportResponseDto;
import noodlezip.report.entity.QReport;
import noodlezip.report.status.ReportStatus;
import noodlezip.report.status.ReportType;
import noodlezip.user.entity.QUser;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static noodlezip.report.entity.QReport.report;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PageImpl<ReportResponseDto> findReportList(Pageable pageable, String type) {
        QUser user = QUser.user;
        QReport report = QReport.report;

        BooleanBuilder condition = new BooleanBuilder(report.reportStatus.eq(ReportStatus.PENDING));

        if (!"ALL".equalsIgnoreCase(type)) {
            try {
                ReportType reportType = ReportType.valueOf(type.toUpperCase());
                condition.and(report.reportType.eq(reportType));
            } catch (IllegalArgumentException e) {
                // 무효한 값이 들어온 경우 예외 처리 또는 무시
            }
        }

        List<ReportResponseDto> content = queryFactory
                .select(Projections.constructor(ReportResponseDto.class,
                        report.id,
                        user.loginId,
                        report.reportType,
                        report.reportTargetId,
                        report.content,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i')", report.createdAt)
                ))
                .from(report)
                .join(user).on(report.user.eq(user))
                .where(condition)
                .orderBy(report.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(report.count())
                .from(report)
                .join(user).on(report.user.eq(user))
                .where(condition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }


}
