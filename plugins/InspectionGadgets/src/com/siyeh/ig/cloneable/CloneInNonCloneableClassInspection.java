package com.siyeh.ig.cloneable;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.*;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.MethodInspection;
import com.siyeh.ig.fixes.MakeCloneableFix;
import com.siyeh.ig.psiutils.CloneUtils;
import org.jetbrains.annotations.NotNull;

public class CloneInNonCloneableClassInspection extends MethodInspection {

    private InspectionGadgetsFix fix = new MakeCloneableFix();
    public String getDisplayName() {
        return "'clone()' method in non-Cloneable class";
    }

    public String getGroupDisplayName() {
        return GroupNames.CLONEABLE_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "#ref() defined in non-Cloneable class #loc";
    }

    protected InspectionGadgetsFix buildFix(PsiElement location){
        return fix;
    }

    public BaseInspectionVisitor buildVisitor() {
        return new CloneInNonCloneableClassVisitor();
    }

    private static class CloneInNonCloneableClassVisitor extends BaseInspectionVisitor {

        public void visitMethod(@NotNull PsiMethod method){
            final PsiClass containingClass = method.getContainingClass();
            final String name = method.getName();
            if(!"clone".equals(name))
            {
                return;
            }
            final PsiParameterList parameterList = method.getParameterList();
            if(parameterList == null)
            {
                return;
            }
            final PsiParameter[] parameters = parameterList.getParameters();
            if(parameters == null || parameters.length!=0)
            {
                return;
            }
            if(containingClass == null)
            {
                return;
            }
            if(CloneUtils.isCloneable(containingClass)){
                return;
            }
            registerMethodError(method);
        }

    }

}
