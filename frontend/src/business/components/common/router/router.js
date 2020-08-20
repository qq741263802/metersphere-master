import Vue from "vue";
import VueRouter from 'vue-router'
import RouterSidebar from "./RouterSidebar";
import EditPerformanceTestPlan from "../../performance/test/EditPerformanceTestPlan";
import PerformanceTestPlan from "../../performance/test/PerformanceTestPlan";
import MsProject from "../../project/MsProject";
import PerformanceChart from "../../performance/report/components/PerformanceChart";
import PerformanceTestReport from "../../performance/report/PerformanceTestReport";
import ApiTest from "../../api/ApiTest";
import PerformanceTest from "../../performance/PerformanceTest";
import ApiTestConfig from "../../api/test/ApiTestConfig";
import PerformanceTestHome from "../../performance/home/PerformanceTestHome";
import ApiTestList from "../../api/test/ApiTestList";
import ApiTestHome from "../../api/home/ApiTestHome";
import PerformanceReportView from "../../performance/report/PerformanceReportView";
import ApiReportView from "../../api/report/ApiReportView";
import TrackHome from "../../track/home/TrackHome";
import TestPlan from "../../track/plan/TestPlan";
import TestPlanView from "../../track/plan/view/TestPlanView";
import TestCase from "../../track/case/TestCase";
import TestTrack from "../../track/TestTrack";
import ApiReportList from "../../api/report/ApiReportList";
import axios from "axios";
import Setting from "@/business/components/settings/router";

Vue.use(VueRouter);

const router = new VueRouter({
  routes: [
    {path: "/", redirect: '/setting/personsetting'},
    {
      path: "/sidebar",
      components: {
        sidebar: RouterSidebar
      }
    },
    Setting,
    {
      path: "/api",
      name: "api",
      redirect: "/api/home",
      components: {
        content: ApiTest
      },
      children: [
        {
          path: 'home',
          name: 'fucHome',
          component: ApiTestHome,
        },
        {
          path: "test/:type",
          name: "ApiTestConfig",
          component: ApiTestConfig,
          props: (route) => ({id: route.query.id})
        },
        {
          path: "test/list/:projectId",
          name: "ApiTestList",
          component: ApiTestList
        },
        {
          path: "project/:type",
          name: "fucProject",
          component: MsProject
        },
        {
          path: "report/list/:testId",
          name: "ApiReportList",
          component: ApiReportList
        },
        {
          path: "report/view/:reportId",
          name: "ApiReportView",
          component: ApiReportView
        }
      ]
    },
    {
      path: "/performance",
      name: "performance",
      redirect: "/performance/home",
      components: {
        content: PerformanceTest
      },
      children: [
        {
          path: 'home',
          name: 'perHome',
          component: PerformanceTestHome,
        },
        {
          path: 'test/create',
          name: "createPerTest",
          component: EditPerformanceTestPlan,
        },
        {
          path: "test/edit/:testId",
          name: "editPerTest",
          component: EditPerformanceTestPlan,
          props: {
            content: (route) => {
              return {
                ...route.params
              }
            }
          }
        },
        {
          path: "test/:projectId",
          name: "perPlan",
          component: PerformanceTestPlan
        },
        {
          path: "project/:type",
          name: "perProject",
          component: MsProject
        },
        {
          path: "report/:type",
          name: "perReport",
          component: PerformanceTestReport
        },
        {
          path: "chart",
          name: "perChart",
          component: PerformanceChart
        },
        {
          path: "report/view/:reportId",
          name: "perReportView",
          component: PerformanceReportView
        }
      ]
    },
    {
      path: "/track",
      name: "track",
      redirect: "/track/home",
      components: {
        content: TestTrack
      },
      children: [
        {
          path: 'home',
          name: 'trackHome',
          component: TrackHome,
        },
        {
          path: 'case/create',
          name: 'testCaseCreate',
          component: TestCase,
        },
        {
          path: 'case/:projectId',
          name: 'testCase',
          component: TestCase,
        },
        {
          path: 'case/edit/:caseId',
          name: 'testCaseEdit',
          component: TestCase,
        },
        {
          path: "plan/:type",
          name: "testPlan",
          component: TestPlan
        },
        {
          path: "plan/view/:planId",
          name: "planView",
          component: TestPlanView
        },
        {
          path: "plan/view/edit/:caseId",
          name: "planViewEdit",
          component: TestPlanView
        },
        {
          path: "project/:type",
          name: "trackProject",
          component: MsProject
        }
      ]
    }
  ]
});


router.beforeEach((to, from, next) => {
  //解决localStorage清空，cookie没失效导致的卡死问题
  if (!localStorage.getItem('Admin-Token')) {
    axios.get("/signout");
    console.log("signout");
    localStorage.setItem('Admin-Token', "{}");
    window.location.href = "/login";
    next();
  } else {
    next();
  }
});

export default router
